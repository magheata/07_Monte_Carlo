/* Created by andreea on 11/06/2020 */
package Domain.Interfaces;

import Domain.Color;
import Domain.FlagColors;

import java.awt.image.BufferedImage;

public interface IColorimetryService {
    FlagColors findColorPercentages(String flagImagePath, BufferedImage image);
    FlagColors findColorPercentagesMonteCarlo(String flagImagePath, BufferedImage image, int samples);
    Color getPixelColor(int r, int g, int b);
}
