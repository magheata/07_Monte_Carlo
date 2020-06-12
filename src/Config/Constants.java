/* Created by andreea on 10/06/2020 */
package Config;

import Domain.Color;

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
}
