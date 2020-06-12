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
import java.util.ArrayList;
import java.util.Iterator;

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

    public void closeConnection() {
        dbManager.closeConnection();
    }

    public void getCountryForFlag(FlagColors selectedFlag){
        ArrayList<String> flags = new ArrayList<>();
        int margin = 5;
        while (flags.isEmpty()){
            flags = dbManager.getFlagsWithinRange(margin, selectedFlag.getRed(), selectedFlag.getOrange(), selectedFlag.getYellow(), selectedFlag.getGreen_1(),
                    selectedFlag.getGreen_2(), selectedFlag.getGreen_3(), selectedFlag.getBlue_1(), selectedFlag.getBlue_2(), selectedFlag.getBlue_3(),
                    selectedFlag.getIndigo(), selectedFlag.getPink(), selectedFlag.getMagenta(), selectedFlag.getBlack(), selectedFlag.getWhite());
            if (flags.isEmpty()){
                margin++;
            }
        }
        System.out.println(flags);
    }

    public void setUseProbabilisticAlgorithm(boolean useProbabilisticAlgorithm) {
        this.useProbabilisticAlgorithm = useProbabilisticAlgorithm;
    }
}
