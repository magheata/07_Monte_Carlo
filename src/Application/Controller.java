/* Created by andreea on 10/06/2020 */
package Application;

import Config.Constants;
import Domain.FlagColors;
import Domain.Interfaces.IController;
import Infrastructure.ColorimetryService.ColorimetryService;
import Infrastructure.DB.DBManager;
import Presentation.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Controller implements IController {

    private ColorimetryService colorimetryService;
    private DBManager dbManager;
    private Window window;
    private ExecutorService executor;
    private boolean useProbabilisticAlgorithm = false;

    public Controller() {
        colorimetryService = new ColorimetryService();
        dbManager = new DBManager();
        if (!dbManager.tableExists(Constants.TABLE_COUNTRY)){
            dbManager.insertValuesIntoCountryTable();
        }
        loadFlagsTable();
        window = new Window(this);
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void loadFlagsTable() {
        if (!dbManager.tableExists(Constants.TABLE_FLAGS)){
            dbManager.createFlagsTable();
            ArrayList<String> countryFlags = dbManager.getAllValuesForColumn("country", "flag");
            Iterator it = countryFlags.iterator();
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

    @Override
    public Future<Object[]> getCountryForFlag(String flagPath, int iterations, int samples) {
        return executor.submit(() -> {
            if (useProbabilisticAlgorithm){
                if (iterations == 0){
                    window.showDialog("Iterations not specified", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if (samples == 0){
                    window.showDialog("Number of samples not specified", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }

            ArrayList<FlagColors> possibleFlags;
            FlagColors flagColors;
            boolean useWhiteFont = false;
            if (!useProbabilisticAlgorithm){
                try {
                    flagColors = colorimetryService.findColorPercentages(flagPath,
                            ImageIO.read(new File(flagPath)));
                    possibleFlags = getPossibleFlags(flagColors);
                    window.showAllPossibleCountries(possibleFlags, getPercentageOfEqualColors(flagColors, possibleFlags), null);
                    if (flagColors.getBlack() > 35){
                        useWhiteFont = true;
                    }
                    return new Object[] {dbManager.getNameOfCountryFlag((String) getFlagWithBestScore(possibleFlags.iterator(), flagColors)[0]),
                            useWhiteFont};
                } catch (IOException e) {}
            } else {
                try {
                    HashMap<String, Integer> flagOccurrences = new HashMap<>();
                    for (int i = 0; i < iterations; i++){
                        flagColors = colorimetryService.findColorPercentagesMonteCarlo(flagPath,
                                ImageIO.read(new File(flagPath)),
                                samples);
                        if (flagColors.getBlack() > 35){
                            useWhiteFont = true;
                        }
                        possibleFlags = getPossibleFlags(flagColors);
                        Object[] bestFlag = getFlagWithBestScore(possibleFlags.iterator(), flagColors);
                        if (flagOccurrences.containsKey(bestFlag[0])){
                            int occurrences = flagOccurrences.get(bestFlag[0]);
                            flagOccurrences.replace((String) bestFlag[0], occurrences + 1);
                        } else {
                            flagOccurrences.put((String) bestFlag[0], 1);
                        }
                    }
                    HashMap<String, Integer> sortedFlags = sortByValue(flagOccurrences);
                    window.showAllPossibleCountries(null, null, sortedFlags);
                    return new Object[] {dbManager.getNameOfCountryFlag((String) sortedFlags.keySet().toArray()[sortedFlags.keySet().size() - 1]),
                            useWhiteFont};
                } catch (IOException e) {}
            }
            return null;
        });
    }

    private HashMap<String, Float> getPercentageOfEqualColors(FlagColors flagColors, ArrayList<FlagColors> possibleFlags){
        HashMap<String, Float> percentages = new HashMap<>();
        for (FlagColors possibleFlag: possibleFlags) {
            percentages.put(possibleFlag.getFlagImagePath(), 100 - getDifferenceBetweenFlagColors(flagColors, possibleFlag));
        }
        return percentages;
    }

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

    @Override
    public String getCountryForFlag(String flagImage) {
        return dbManager.getNameOfCountryFlag(flagImage);
    }

    // function to sort hashmap by values
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list = new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

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

    @Override
    public void closeConnection() {
        dbManager.closeConnection();
    }

    public ArrayList<FlagColors> getPossibleFlags(FlagColors selectedFlag){
        ArrayList<FlagColors> flags = new ArrayList<>();
        float margin = window.getMargin();
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
            if (flags.isEmpty()){
                margin++;
            }
        }
        return flags;
    }

    public void setUseProbabilisticAlgorithm(boolean useProbabilisticAlgorithm) {
        this.useProbabilisticAlgorithm = useProbabilisticAlgorithm;
    }
}
