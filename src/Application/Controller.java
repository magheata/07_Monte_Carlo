/* Created by andreea on 10/06/2020 */
package Application;

import Config.Constants;
import Domain.FlagColors;
import Domain.Interfaces.IController;
import Infrastructure.ColorimetryService.ColorimetryService;
import Infrastructure.DB.DBManager;
import Presentation.Window;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Controller implements IController {

    private ColorimetryService colorimetryService;
    private DBManager dbManager;
    private Window window;
    private boolean useProbabilisticAlgorithm = false;

    public Controller() {
        colorimetryService = new ColorimetryService();
        dbManager = new DBManager();
        if (!dbManager.tableExists(Constants.TABLE_COUNTRY)){
            dbManager.insertValuesIntoCountryTable();
        }
        loadFlagsTable();
        window = new Window(this);
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
    public String getCountryForFlag(String flagPath, int iterations, int samples) {
        ArrayList<FlagColors> possibleFlags = null;
        FlagColors flagColors;
        if (!useProbabilisticAlgorithm){
            try {
                flagColors = colorimetryService.findColorPercentages(flagPath,
                        ImageIO.read(new File(flagPath)));
                possibleFlags = getPossibleFlags(flagColors);
                window.showAllPossibleCountries(possibleFlags, null);
                return dbManager.getNameOfCountryFlag((String) getFlagWithBestScore(possibleFlags.iterator(), flagColors)[0]);
            } catch (IOException e) {}
        } else {
            try {
                HashMap<String, Integer> flagOccurrences = new HashMap<>();
                for (int i = 0; i < iterations; i++){
                    flagColors = colorimetryService.findColorPercentagesMonteCarlo(flagPath,
                            ImageIO.read(new File(flagPath)),
                            samples);
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
                System.out.println(sortedFlags);
                window.showAllPossibleCountries(null, sortedFlags);
                return dbManager.getNameOfCountryFlag((String) sortedFlags.keySet().toArray()[sortedFlags.keySet().size() - 1]);
            } catch (IOException e) {}

        }
        return null;
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
        int bestScore = 0;
        String filePathCorrectFlag = "";
        while (iterator.hasNext()){
            FlagColors possibleFlag = (FlagColors) iterator.next();
            int flagScore = getTotalScoreForThisFlag(flagColors, possibleFlag);
            if (flagScore > bestScore){
                bestScore = flagScore;
                filePathCorrectFlag = possibleFlag.getFlagImagePath();
            }
        }
        return new Object[]{filePathCorrectFlag, bestScore};
    }

    private int getTotalScoreForThisFlag(FlagColors actualFlag, FlagColors possiblePath) {
        int score = 0;
        if (withinRange(actualFlag.getRed(), possiblePath.getRed())){
            score++;
        }
        if (withinRange(actualFlag.getOrange(), possiblePath.getOrange())){
            score++;
        }if (withinRange(actualFlag.getYellow(), possiblePath.getYellow())){
            score++;
        }if (withinRange(actualFlag.getGreen_1(), possiblePath.getGreen_1())){
            score++;
        }if (withinRange(actualFlag.getGreen_2(), possiblePath.getGreen_2())){
            score++;
        }if (withinRange(actualFlag.getGreen_3(), possiblePath.getGreen_3())){
            score++;
        }if (withinRange(actualFlag.getBlue_1(), possiblePath.getBlue_1())){
            score++;
        }if (withinRange(actualFlag.getBlue_2(), possiblePath.getBlue_2())){
            score++;
        }if (withinRange(actualFlag.getBlue_3(), possiblePath.getBlue_3())){
            score++;
        }if (withinRange(actualFlag.getIndigo(), possiblePath.getIndigo())){
            score++;
        }if (withinRange(actualFlag.getPink(), possiblePath.getPink())){
            score++;
        }if (withinRange(actualFlag.getMagenta(), possiblePath.getMagenta())){
            score++;
        }if (withinRange(actualFlag.getBlack(), possiblePath.getBlack())){
            score++;
        }if (withinRange(actualFlag.getWhite(), possiblePath.getWhite())){
            score++;
        }
        return score;
    }

    private boolean withinRange(float goalValue, float valueToCheck){
        return (valueToCheck >= (goalValue - 3.5)) &&
                ((goalValue + 3.5) >= valueToCheck);
    }

    @Override
    public void closeConnection() {
        dbManager.closeConnection();
    }

    public ArrayList<FlagColors> getPossibleFlags(FlagColors selectedFlag){
        ArrayList<FlagColors> flags = new ArrayList<>();
        float margin = 5.5f;
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
