/* Created by andreea on 11/06/2020 */
package Domain;

/**
 * Class used to define a HSB color
 */
public class HSBColor {
    private float hue;
    private float saturation;
    private float brightness;

    public HSBColor(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    /**
     * Checks if the color is white
     * @return
     */
    public boolean isWhite(){
        return (hue == 0) && (saturation == 0) && (brightness == 100);
    }

    /**
     * Checks if the color is black
     * @return
     */
    public boolean isBlack(){
        return brightness == 0;
    }

    public float getHue() {
        return hue;
    }
}
