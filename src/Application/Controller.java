/* Created by andreea on 10/06/2020 */
package Application;

import Config.Constants;
import Domain.FlagColors;
import Domain.Interfaces.IController;
import Infrastructure.ColorimetryService.ColorimetryService;
import Infrastructure.DB.DBManager;

import javax.imageio.ImageIO;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Controller implements IController {

    private ColorimetryService colorimetryService;
    private DBManager dbManager;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public Controller() {
        colorimetryService = new ColorimetryService();
        dbManager = new DBManager();
        if (!dbManager.tableExists(Constants.TABLE_COUNTRY)){
            dbManager.insertValuesIntoCountryTable();
        }
        loadFlagsTable();
        changes.firePropertyChange("tablaCreada", false, true);
    }

    @Override
    public void loadFlagsTable() {
        if (!dbManager.tableExists(Constants.TABLE_FLAGS)){
            dbManager.createFlagsTable();
            ArrayList<String> countryFlags = dbManager.getAllValuesForColumn("country", "flag");
            Iterator it = countryFlags.iterator();
            while (it.hasNext()){
                String flagImage = (String) it.next();
                FlagColors flagColors = findColorPercentageOfImage(flagImage);
                if (flagColors != null){
                    dbManager.insertValuesIntroFlafTable(flagImage, flagColors.getRed(), flagColors.getOrange(),
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
                return colorimetryService.findColorPercentages(ImageIO.read(new File(Constants.USER_PATH + "/flags/" + fileName)));
            }
        } catch (IOException e) {
        }
        return null;
    }
}
