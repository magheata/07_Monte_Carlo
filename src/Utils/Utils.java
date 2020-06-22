/* Created by andreea on 21/06/2020 */
package Utils;

import Config.Constants;
import Domain.FlagColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Utils {
    // function to sort hashmap by values
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
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

    public static Map<String, Float> sortByComparator(Map<String, Float> unsortMap, final boolean order) {

        ArrayList<Map.Entry<String, Float>> list = new ArrayList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, (o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());

            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Float> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Float> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static boolean useWhiteFont(FlagColors flagColors){
        return flagColors.getBlack() > Constants.BLACK_LIMIT;
    }

    public static BufferedImage resize(BufferedImage image, int areaWidth, int areaHeight) {
        float scaleX = (float) areaWidth / image.getWidth();
        float scaleY = (float) areaHeight / image.getHeight();
        float scale = Math.min(scaleX, scaleY);
        int w = Math.round(image.getWidth() * scale);
        int h = Math.round(image.getHeight() * scale);

        int type = image.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

        boolean scaleDown = scale < 1;

        if (scaleDown) {
            // multi-pass bilinear div 2
            int currentW = image.getWidth();
            int currentH = image.getHeight();
            BufferedImage resized = image;
            while (currentW > w || currentH > h) {
                currentW = Math.max(w, currentW / 2);
                currentH = Math.max(h, currentH / 2);

                BufferedImage temp = new BufferedImage(currentW, currentH, type);
                Graphics2D g2 = temp.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(resized, 0, 0, currentW, currentH, null);
                g2.dispose();
                resized = temp;
            }
            return resized;
        } else {
            Object hint = scale > 2 ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_BILINEAR;

            BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resized.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(image, 0, 0, w, h, null);
            g2.dispose();
            return resized;
        }
    }
}
