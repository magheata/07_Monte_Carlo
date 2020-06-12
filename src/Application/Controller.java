/* Created by andreea on 10/06/2020 */
package Application;

import Config.Constants;
import Domain.Interfaces.IController;
import Infrastructure.ColorimetryService.ColorimetryService;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller implements IController {

    private ColorimetryService colorimetryService;

    public Controller() {
        colorimetryService = new ColorimetryService();
    }

    @Override
    public void findColorPercentageOfImage(String fileName) {
        try {
            colorimetryService.findColorPercentages(ImageIO.read(new File(Constants.USER_PATH + "/flags/" + fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
