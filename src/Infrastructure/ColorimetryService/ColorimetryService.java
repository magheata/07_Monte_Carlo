/* Created by andreea on 11/06/2020 */
package Infrastructure.ColorimetryService;

import Config.Constants;
import Domain.Color;
import Domain.FlagColors;
import Domain.HSBColor;
import Domain.Interfaces.IColorimetryService;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ColorimetryService implements IColorimetryService {

    private HSBConverter hsbConverter;

    private int totalPixels;
    public ColorimetryService(){
        hsbConverter = new HSBConverter();
    }

    // "Tradicional"
    @Override
    public FlagColors findColorPercentages(String flagImagePath, BufferedImage image){
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

        totalPixels = image.getWidth() * image.getHeight();

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

        return new FlagColors(flagImagePath,
                getColorPercentage(redCount, totalPixels),
                getColorPercentage(orangeCount, totalPixels),
                getColorPercentage(yellowCount, totalPixels),
                getColorPercentage(green1Count, totalPixels),
                getColorPercentage(green2Count, totalPixels),
                getColorPercentage(green3Count, totalPixels),
                getColorPercentage(blue1Count, totalPixels),
                getColorPercentage(blue2Count, totalPixels),
                getColorPercentage(blue3Count, totalPixels),
                getColorPercentage(indigoCount, totalPixels),
                getColorPercentage(pinkCount, totalPixels),
                getColorPercentage(magentaCount, totalPixels),
                getColorPercentage(blackCount, totalPixels),
                getColorPercentage(whiteCount, totalPixels));
    }

    private float getColorPercentage(float colorPixels, int totalPixels){
        return (colorPixels / totalPixels) * 100;
    }

    @Override
    public FlagColors findColorPercentagesMonteCarlo(String flagImagePath, BufferedImage image, int samples) {
        ArrayList<Object[]> selectedPositions = new ArrayList<>();
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

        totalPixels = image.getWidth() * image.getHeight();

        for (int i = 0; i < samples; i++){
            int random_x = ThreadLocalRandom.current().nextInt(0, image.getWidth());
            int random_y = ThreadLocalRandom.current().nextInt(0, image.getHeight());

            while (selectedPixelUsed(selectedPositions, random_x, random_y)){
                random_x = ThreadLocalRandom.current().nextInt(0, image.getWidth());
                random_y = ThreadLocalRandom.current().nextInt(0, image.getHeight());
            }

            int clr = image.getRGB(random_x, random_y);

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

        return new FlagColors(flagImagePath,
                getColorPercentage(redCount, samples),
                getColorPercentage(orangeCount, samples),
                getColorPercentage(yellowCount, samples),
                getColorPercentage(green1Count, samples),
                getColorPercentage(green2Count, samples),
                getColorPercentage(green3Count, samples),
                getColorPercentage(blue1Count, samples),
                getColorPercentage(blue2Count, samples),
                getColorPercentage(blue3Count, samples),
                getColorPercentage(indigoCount, samples),
                getColorPercentage(pinkCount, samples),
                getColorPercentage(magentaCount, samples),
                getColorPercentage(blackCount, samples),
                getColorPercentage(whiteCount, samples));
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

    private boolean selectedPixelUsed(ArrayList<Object[]> pixels, int x, int y){
        for (Object[] pixelPos : pixels){
            if ((x == (Integer) pixelPos[0]) && (y == (Integer) pixelPos[1])){
                return true;
            }
        }
        return false;
    }
}
