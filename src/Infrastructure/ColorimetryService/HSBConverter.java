/* Created by andreea on 11/06/2020 */
package Infrastructure.ColorimetryService;

import Domain.HSBColor;

public class HSBConverter {

    public HSBColor rgbToHsb(int r, int g, int b){
        float [] hsbValues = java.awt.Color.RGBtoHSB(r, g, b, null);
        return new HSBColor(hsbValues[0] * 360, hsbValues[1] * 100, hsbValues[2] * 100);
    }
}
