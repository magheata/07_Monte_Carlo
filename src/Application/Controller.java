/* Created by andreea on 10/06/2020 */
package Application;

import Config.Constants;
import Domain.FlagColors;
import Domain.Interfaces.IController;
import Infrastructure.ColorimetryService.ColorimetryService;
import Infrastructure.DB.DBManager;
import Presentation.Window;
import Utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class that implements the necessary functions for the communication bewteen the Presentation and Model. It is also
 * in charge of dealing with the database connections and queries.
 */
public class Controller implements IController {

    private ColorimetryService colorimetryService;
    private DBManager dbManager;
    private Window window;
    private ExecutorService executor;
    private boolean useProbabilisticAlgorithm = false;

    /**
     * Constructor. Creates the instances of the needed classes and loads the databases in case
     * they are not available.
     */
    public Controller() {
        executor = Executors.newSingleThreadExecutor();
        colorimetryService = new ColorimetryService();
        dbManager = new DBManager();
        // Checks if the databases are created
        checkDatabases();
        window = new Window(this);
    }

    /**
     * Closes the database connection.
     */
    @Override
    public void closeConnection() {
        dbManager.closeConnection();
    }

    /**
     * Calculates the percentage of each color in the given flag and returns a new FlabColors object containing
     * the percentages.
     * @param fileName flag file
     * @return FlagColors object containing the colors' information, null if the fileName is empty
     */
    @Override
    public FlagColors findColorPercentageOfImage(String fileName) {
        try {
            if (!fileName.isEmpty()){
                return colorimetryService.findColorPercentages(fileName, ImageIO.read(new File(fileName)));
            }
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Method that finds the countries that have the most % of colors match the given flag's colors.
     * Depending on the algorithm choosed by the user (traditional or probabilistic), it will use the iterations
     * and samples parameters in the calculation.
     * @param flagPath file name of the chosen flag
     * @param iterations number of times the algorithm will calculate the similar flags in order to get the flag's country
     * @param samples number of samples to take from the flag in order to obtain the colors that have to be analyzed
     * @return 1. list of countries that have flags similar to the chosen flag
     *         2. boolean that determines if the name of the country has to be displayed in white
     */
    @Override
    public Future<Object[]> getCountryForFlag(String flagPath, int iterations, int samples) {
        return executor.submit(() -> {
            // Check if the parameters are correct
            if (parametersCorrect(iterations, samples)){
                ArrayList<FlagColors> possibleFlags;
                FlagColors flagColors = null;
                // Traditional algorithm
                if (!useProbabilisticAlgorithm){
                    try {
                        // Get the colors' information for the flag
                        flagColors = colorimetryService.findColorPercentages(flagPath, ImageIO.read(new File(flagPath)));
                        // Obtain the similar flags from the database
                        possibleFlags = getPossibleFlags(flagColors);
                        // Show all the flags found on screen with the % of match
                        window.showAllPossibleCountries(possibleFlags, getPercentageOfEqualColors(flagColors, possibleFlags), null);
                        // Return the list of countries and if the country has to be displayed in white
                        return new Object[] {dbManager.getNameOfCountryFlag((String) getFlagWithBestScore(possibleFlags.iterator(), flagColors)[0]),
                                Utils.useWhiteFont(flagColors)};
                    } catch (IOException e) {}

                // Probabilistic algorithm
                } else {
                    try {
                        // Create map where the occurrences of each country will be stored
                        HashMap<String, Integer> flagOccurrences = new HashMap<>();
                        // For as many iterations as provided execute the algorithm
                        for (int i = 0; i < iterations; i++){
                            // Find color information of flag
                            flagColors = colorimetryService.findColorPercentagesMonteCarlo(flagPath,
                                    ImageIO.read(new File(flagPath)),
                                    samples);
                            // Get similar flags from database
                            possibleFlags = getPossibleFlags(flagColors);
                            // Obtain the flag that matches the most
                            Object[] bestFlag = getFlagWithBestScore(possibleFlags.iterator(), flagColors);
                            // If the country has already been added to the map, increase its occurrences
                            if (flagOccurrences.containsKey(bestFlag[0])){
                                int occurrences = flagOccurrences.get(bestFlag[0]);
                                flagOccurrences.replace((String) bestFlag[0], occurrences + 1);
                            } else {
                                // Add the new contry to the map with 1 occurrence
                                flagOccurrences.put((String) bestFlag[0], 1);
                            }
                        }
                        // Sort list of occurrences
                        HashMap<String, Integer> sortedFlags = Utils.sortByValue(flagOccurrences);
                        // Show on screen all the similar flags with their occurrences
                        window.showAllPossibleCountries(null, null, sortedFlags);
                        // Return the list of countries and if the country has to be displayed in white
                        return new Object[] {dbManager.getNameOfCountryFlag((String) sortedFlags.keySet().toArray()[sortedFlags.keySet().size() - 1]),
                                Utils.useWhiteFont(flagColors)};
                    } catch (IOException e) {}
                }
            }
            // If probabilistic algorithm used and parameters are not correct, return null
            return null;
        });
    }

    /**
     * Get the name of the country given the file name of the flag.
     * @param flagImage file name
     * @return country name
     */
    @Override
    public String getCountryForFlag(String flagImage) {
        return dbManager.getNameOfCountryFlag(flagImage);
    }

    /**
     * Method used to create the flags table in the database.
     */
    @Override
    public void loadFlagsTable() {
        // Only creates it if the table does not exist
        if (!dbManager.tableExists(Constants.TABLE_FLAGS)){
            // Create the table that will contain the information of the colors of each flag
            dbManager.createFlagsTable();
            // Get all flags existing in the Country table
            ArrayList<String> countryFlags = dbManager.getAllValuesForColumn("country", "flag");
            Iterator it = countryFlags.iterator();
            // For each flag calculate the % of colors and add the result to the Flags table
            while (it.hasNext()){
                String flagImage = (String) it.next();
                FlagColors flagColors = findColorPercentageOfImage(Constants.USER_PATH + "/flags/" + flagImage);
                if (flagColors != null){
                    dbManager.insertValuesIntoFlagTable(flagImage, flagColors.getRed(), flagColors.getOrange(),
                            flagColors.getYellow(), flagColors.getGreen_1(), flagColors.getGreen_2(), flagColors.getGreen_3(),
                            flagColors.getBlue_1(), flagColors.getBlue_2(), flagColors.getBlue_3(), flagColors.getIndigo(),
                            flagColors.getPink(), flagColors.getMagenta(), flagColors.getBlack(), flagColors.getWhite());
                }
            }
        }
    }

    //region PRIVATE METHODS

    /**
     * Checks if the database is created and filled
     */
    private void checkDatabases(){
        executor.submit(() ->{
            // Check to see if databases are created
            if (!dbManager.tableExists(Constants.TABLE_COUNTRY)){
                // If not, create tables Country and Flags
                dbManager.insertValuesIntoCountryTable();
            }
            loadFlagsTable();
        });
    }

    /**
     * Calculates the total difference in the flags' colors. It adds the total difference bewteen each color and returns
     * it
     * @param flagColors flag chosen
     * @param possibleFlag possible flag that might be the same as the chosen one
     * @return total sum of the difference bewteen the colors
     */
    private float getDifferenceBetweenFlagColors(FlagColors flagColors, FlagColors possibleFlag){
        float percentageColors = 0;
        percentageColors = percentageColors + Math.abs(flagColors.getRed() - possibleFlag.getRed());
        percentageColors = percentageColors + Math.abs(flagColors.getOrange() - possibleFlag.getOrange());
        percentageColors = percentageColors + Math.abs(flagColors.getYellow() - possibleFlag.getYellow());
        percentageColors = percentageColors + Math.abs(flagColors.getGreen_1() - possibleFlag.getGreen_1());
        percentageColors = percentageColors + Math.abs(flagColors.getGreen_2() - possibleFlag.getGreen_2());
        percentageColors = percentageColors + Math.abs(flagColors.getGreen_3() - possibleFlag.getGreen_3());
        percentageColors = percentageColors + Math.abs(flagColors.getBlue_1() - possibleFlag.getBlue_1());
        percentageColors = percentageColors + Math.abs(flagColors.getBlue_2() - possibleFlag.getBlue_2());
        percentageColors = percentageColors + Math.abs(flagColors.getBlue_3() - possibleFlag.getBlue_3());
        percentageColors = percentageColors + Math.abs(flagColors.getIndigo() - possibleFlag.getIndigo());
        percentageColors = percentageColors + Math.abs(flagColors.getPink() - possibleFlag.getPink());
        percentageColors = percentageColors + Math.abs(flagColors.getMagenta() - possibleFlag.getMagenta());
        percentageColors = percentageColors + Math.abs(flagColors.getWhite() - possibleFlag.getWhite());
        percentageColors = percentageColors + Math.abs(flagColors.getBlack() - possibleFlag.getBlack());
        return percentageColors;
    }

    /**
     * Calculates the flag that has the least difference in colors compared to the chosen flag.
     * @param iterator iterator for the list of possible countries
     * @param flagColors flag chosen by user
     * @return 1. file name of the most similar flag
     *         2. sum of the total differences in the colors of the compared flags
     */
    private Object[] getFlagWithBestScore(Iterator iterator, FlagColors flagColors){
        float bestScore = Float.MAX_VALUE;
        String filePathCorrectFlag = "";
        while (iterator.hasNext()){
            FlagColors possibleFlag = (FlagColors) iterator.next();
            float flagScore = getDifferenceBetweenFlagColors(flagColors, possibleFlag);
            if (flagScore < bestScore){
                bestScore = flagScore;
                filePathCorrectFlag = possibleFlag.getFlagImagePath();
            }
        }
        return new Object[]{filePathCorrectFlag, bestScore};
    }

    /**
     * Calculates the % of matched colors for each flag and saves it inside a HashMap.
     * @param flagColors flag chosen by user
     * @param possibleFlags list of possible flags that are similar to the chosen flag
     * @return
     */
    private HashMap<String, Float> getPercentageOfEqualColors(FlagColors flagColors, ArrayList<FlagColors> possibleFlags){
        HashMap<String, Float> percentages = new HashMap<>();
        for (FlagColors possibleFlag: possibleFlags) {
            // calulates the % of match in the colors
            percentages.put(possibleFlag.getFlagImagePath(), 100 - getDifferenceBetweenFlagColors(flagColors, possibleFlag));
        }
        // returns the map
        return percentages;
    }

    /**
     * Selects from the database the flags that are within the specified margin of error.
     * @param selectedFlag selected flag by the user
     * @return list of FlagColors objects corresponding to the selected flags from the database
     */
    private ArrayList<FlagColors> getPossibleFlags(FlagColors selectedFlag){
        ArrayList<FlagColors> flags = new ArrayList<>();
        float margin = window.getMargin();
        // We select the flags, in case the margin is too narrow we increase it until we get flags
        while (flags.isEmpty()){
            flags = dbManager.getFlagsWithinRange(margin,
                    selectedFlag.getRed(),
                    selectedFlag.getOrange(),
                    selectedFlag.getYellow(),
                    selectedFlag.getGreen_1(),
                    selectedFlag.getGreen_2(),
                    selectedFlag.getGreen_3(),
                    selectedFlag.getBlue_1(),
                    selectedFlag.getBlue_2(),
                    selectedFlag.getBlue_3(),
                    selectedFlag.getIndigo(),
                    selectedFlag.getPink(),
                    selectedFlag.getMagenta(),
                    selectedFlag.getBlack(),
                    selectedFlag.getWhite());
            // If no flags were found, increase the margin
            if (flags.isEmpty()){
                margin++;
            }
        }
        // return the list of flags similar to the chosen flag
        return flags;
    }

    /**
     * Checks if the iterations and samples are valid. If they aren't, it shows a message to the user with the problem
     * @param iterations
     * @param samples
     * @return
     */
    private boolean parametersCorrect(int iterations, int samples){
        if (useProbabilisticAlgorithm){
            if (iterations == 0){
                SwingUtilities.invokeLater(() -> {
                    window.showDialog("Iterations not specified", "Error", JOptionPane.ERROR_MESSAGE);
                });
                return false;
            }
            if (samples == 0){
                SwingUtilities.invokeLater(() -> {
                    window.showDialog("Number of samples not specified", "Error", JOptionPane.ERROR_MESSAGE);
                });
                return false;
            }
        }
        return true;
    }
    //endregion

    //region GETTERS & SETTERS
    public void setUseProbabilisticAlgorithm(boolean useProbabilisticAlgorithm) {
        this.useProbabilisticAlgorithm = useProbabilisticAlgorithm;
    }
    //endregion
}
