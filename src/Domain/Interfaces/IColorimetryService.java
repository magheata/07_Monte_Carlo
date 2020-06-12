/* Created by andreea on 11/06/2020 */
package Domain.Interfaces;

import Domain.Color;

import java.awt.image.BufferedImage;

public interface IColorimetryService {
    void findColorPercentages(BufferedImage image);
    void findColorPercentagesMonteCarlo();

    Color getPixelColor(int r, int g, int b);
}
