/* Created by andreea on 11/06/2020 */
package Infrastructure.ColorimetryService;

import Config.Constants;
import Domain.Color;
import Domain.HSBColor;
import Domain.Interfaces.IColorimetryService;

import java.awt.image.BufferedImage;

/**
 * 0 - RED
 * 30 - ORANGE
 * 60 - YELLOW
 * 90 - GREEN 1
 * 120 - GREEN 2
 * 150 - GREEN 3
 * 180 - BLUE 1
 * 210 - BLUE 2
 * 240 - BLUE 3
 * 270 - INDIGO
 * 300 - PINK
 * 330 - MAGENTA
 */

public class ColorimetryService implements IColorimetryService {

    private HSBConverter hsbConverter;

    public ColorimetryService(){
        hsbConverter = new HSBConverter();
    }

    // "Tradicional"
    @Override
    public void findColorPercentages(BufferedImage image){
        float redCount = 0,
                orangeCount = 0,
                yellowCount = 0,
                green1Count = 0,
                green2Count = 0,
                green3Count = 0,
                blue1Count = 0,
                blue2Count = 0,
                blue3Count  = 0,
                indigoCount = 0,
                pinkCount = 0,
                magentaCount = 0,
                whiteCount = 0,
                blackCount = 0;
        int totalPixels = image.getWidth() * image.getHeight();

        for (int x = 0; x < image.getWidth(); x++){
            for (int y = 0; y < image.getHeight(); y++){
                // Getting pixel color by position x and y
                int clr = image.getRGB(x, y);
                int red =   (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue =   clr & 0x000000ff;
                Color pixelColor = getPixelColor(red, green, blue);
                switch (pixelColor){
                    case BLACK:
                        blackCount++;
                        break;
                    case WHITE:
                        whiteCount++;
                        break;
                    case RED:
                        redCount++;
                        break;
                    case ORANGE:
                        orangeCount++;
                        break;
                    case YELLOW:
                        yellowCount++;
                        break;
                    case GREEN_1:
                        green1Count++;
                        break;
                    case GREEN_2:
                        green2Count++;
                        break;
                    case GREEN_3:
                        green3Count++;
                        break;
                    case BLUE_1:
                        blue1Count++;
                        break;
                    case BLUE_2:
                        blue2Count++;
                        break;
                    case BLUE_3:
                        blue3Count++;
                        break;
                    case INDIGO:
                        indigoCount++;
                        break;
                    case PINK:
                        pinkCount++;
                        break;
                    case MAGENTA:
                        magentaCount++;
                        break;
                }
            }
        }

        if (redCount != 0){
            System.out.println("Red percentage:" + ((redCount / totalPixels) * 100) );
        }
        if (orangeCount != 0){
            System.out.println("Orange percentage:" + ((orangeCount / totalPixels) * 100) );
        }
        if (yellowCount != 0){
            System.out.println("Yellow percentage:" + ((yellowCount / totalPixels) * 100) );
        }
        if (blue1Count != 0 || blue2Count != 0 || blue3Count != 0){
            System.out.println("Blue percentage:" + (((blue1Count + blue2Count + blue3Count) / totalPixels) * 100) );
        }
        if (green1Count != 0 || green2Count != 0 || green3Count != 0){
            System.out.println("Green percentage:" + (((green1Count + green2Count + green3Count) / totalPixels) * 100) );
        }
        if (indigoCount != 0){
            System.out.println("Indigo percentage:" + ((indigoCount / totalPixels) * 100) );
        }
        if (pinkCount != 0){
            System.out.println("Pink percentage:" + ((pinkCount / totalPixels) * 100) );
        }
        if (magentaCount != 0){
            System.out.println("Magenta percentage:" + ((magentaCount / totalPixels) * 100) );
        }
        if (whiteCount != 0){
            System.out.println("White percentage:" + ((whiteCount / totalPixels) * 100) );
        }
        if (blackCount != 0){
            System.out.println("Black percentage:" + ((blackCount / totalPixels) * 100) );
        }
    }

    @Override
    public void findColorPercentagesMonteCarlo() {

    }

    @Override
    public Color getPixelColor(int r, int g, int b){
        HSBColor hsbColor = hsbConverter.rgbToHsb(r, g, b);
        if (hsbColor.isBlack()){
            return Color.BLACK;
        }
        if (hsbColor.isWhite()){
            return Color.WHITE;
        }
        float hue = hsbColor.getHue();
        for (int color : Constants.HSB_PALETTE){
            if (isColor(color, hue)){
                return Constants.HSB_COLORS.get(color);
            }
        }
        return null;
    }

    private boolean isColor(int color, float hue){
        int hueAux = 0;
        boolean useAux = false;
        if (hue + Constants.COLOR_MARGIN > 360){
            useAux = true;
            hueAux = 360;
        }
        if (useAux){
            return ((((hueAux - Constants.COLOR_MARGIN) < hue)) ||
                    ((color <= hue) && (hue <= color + Constants.COLOR_MARGIN)));
        }
        return ((((color - Constants.COLOR_MARGIN) < hue) && (hue <= color)) ||
                ((color <= hue) && (hue <= color + Constants.COLOR_MARGIN)));
    }
}
