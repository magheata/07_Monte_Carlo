/* Created by andreea on 10/06/2020 */
package Config;

import Domain.Color;

import java.awt.*;
import java.io.File;
import java.util.AbstractMap;
import java.util.Map;

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

public class Constants {

    public static final int WIDTH_WINDOW = 600;
    public static final int HEIGHT_WINDOW = 800;
    public static final int WIDTH_CONTROL_PANEL = WIDTH_WINDOW;
    public static final int HEIGHT_CONTROL_PANEL = (int) (HEIGHT_WINDOW * 0.20);
    public static final int WIDTH_FILE_PANEL = WIDTH_WINDOW;
    public static final int HEIGHT_FILE_PANEL = (int) (HEIGHT_WINDOW * 0.60);
    public static final int WIDTH_SCROLL_PANEL = WIDTH_WINDOW;
    public static final int HEIGHT_SCROLL_PANEL = (int) (HEIGHT_WINDOW * 0.20);

    public static final int WIDTH_ITERATIONS_PANEL = 600;
    public static final int HEIGHT_ITERATIONS_PANEL = 50;

    public static final int WIDTH_SAMPLES_PANEL = 600;
    public static final int HEIGHT_SAMPLES_PANEL = 50;

    public static final int WIDTH_SLIDER = 300;
    public static final int HEIGHT_SLIDER = 50;

    public static final int WIDTH_BUTTON = WIDTH_WINDOW;
    public static final int HEIGHT_BUTTON = 25;

    public static final Dimension DIM_WINDOW = new Dimension(WIDTH_WINDOW, HEIGHT_WINDOW);
    public static final Dimension DIM_CONTROL_PANEL = new Dimension(WIDTH_CONTROL_PANEL, HEIGHT_CONTROL_PANEL);
    public static final Dimension DIM_FILE_PANEL = new Dimension(WIDTH_FILE_PANEL, HEIGHT_FILE_PANEL);
    public static final Dimension DIM_ITERATIONS_PANEL = new Dimension(WIDTH_ITERATIONS_PANEL, HEIGHT_ITERATIONS_PANEL);
    public static final Dimension DIM_SAMPLES_PANEL = new Dimension(WIDTH_SAMPLES_PANEL, HEIGHT_SAMPLES_PANEL);
    public static final Dimension DIM_SLIDER = new Dimension(WIDTH_SLIDER, HEIGHT_SLIDER);
    public static final Dimension DIM_BUTTON = new Dimension(WIDTH_BUTTON, HEIGHT_BUTTON);
    public static final Dimension DIM_SCROLL_PANEL = new Dimension(WIDTH_SCROLL_PANEL, HEIGHT_SCROLL_PANEL);

    public static final int COLOR_MARGIN = 15;

    public static final int HSB_RED = 0;
    public static final int HSB_ORANGE = 30;
    public static final int HSB_YELLOW = 60;
    public static final int HSB_GREEN_1 = 90;
    public static final int HSB_GREEN_2 = 120;
    public static final int HSB_GREEN_3 = 150;
    public static final int HSB_BLUE_1 = 180;
    public static final int HSB_BLUE_2 = 210;
    public static final int HSB_BLUE_3 = 240;
    public static final int HSB_INDIGO = 270;
    public static final int HSB_PINK = 300;
    public static final int HSB_MAGENTA = 330;

    public static final Map<Integer, Color> HSB_COLORS = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(HSB_RED, Color.RED),
            new AbstractMap.SimpleEntry<>(HSB_ORANGE, Color.ORANGE),
            new AbstractMap.SimpleEntry<>(HSB_YELLOW, Color.YELLOW),
            new AbstractMap.SimpleEntry<>(HSB_GREEN_1, Color.GREEN_1),
            new AbstractMap.SimpleEntry<>(HSB_GREEN_2, Color.GREEN_2),
            new AbstractMap.SimpleEntry<>(HSB_GREEN_3, Color.GREEN_3),
            new AbstractMap.SimpleEntry<>(HSB_BLUE_1, Color.BLUE_1),
            new AbstractMap.SimpleEntry<>(HSB_BLUE_2, Color.BLUE_2),
            new AbstractMap.SimpleEntry<>(HSB_BLUE_3, Color.BLUE_3),
            new AbstractMap.SimpleEntry<>(HSB_INDIGO, Color.INDIGO),
            new AbstractMap.SimpleEntry<>(HSB_PINK, Color.PINK),
            new AbstractMap.SimpleEntry<>(HSB_MAGENTA, Color.MAGENTA)
            );

    public static final int ERROR_CODE_TABLE_NOT_FOUND = 42102;

    public static final int [] HSB_PALETTE = new int[]{
            HSB_RED,
            HSB_ORANGE,
            HSB_YELLOW,
            HSB_GREEN_1, HSB_GREEN_2, HSB_GREEN_3,
            HSB_BLUE_1, HSB_BLUE_2, HSB_BLUE_3,
            HSB_INDIGO,
            HSB_PINK,
            HSB_MAGENTA
    };

    public static final String USER_PATH = new File("").getAbsolutePath();
    public static final String FILE_COUNTRY_NAMES = "country_names.txt";
    public static final String FILE_COUNTRY_CODES = "country_codes.txt";

    public static final String TABLE_COUNTRY = "country";
    public static final String TABLE_FLAGS = "flagsColorimetry";

    public static final String TEXT_TRY_AGAIN_BUTTON = "Try again with another flag";
    public static final String TEXT_FIND_COUNTRY_BUTTON = "Find flag's country";

    public static final Font FONT_LABEL = new Font("Sans", Font.BOLD, 90);
    public static final Font FONT_SMALL_LABEL = new Font("Sans", Font.BOLD, 40);

}
