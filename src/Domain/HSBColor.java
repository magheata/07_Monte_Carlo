/* Created by andreea on 11/06/2020 */
package Domain;

public class HSBColor {
    private float hue;
    private float saturation;
    private float brightness;

    public HSBColor(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public boolean isWhite(){
        return (hue == 0) && (saturation == 0) && (brightness == 100);
    }

    public boolean isBlack(){
        return brightness == 0;
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}
