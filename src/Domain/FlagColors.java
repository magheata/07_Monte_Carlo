/* Created by andreea on 12/06/2020 */
package Domain;

public class FlagColors {
    private String flagImagePath;
    private float red;
    private float orange;
    private float yellow;
    private float green_1;
    private float green_2;
    private float green_3;
    private float blue_1;
    private float blue_2;
    private float blue_3;
    private float indigo;
    private float pink;
    private float magenta;
    private float black;
    private float white;

    public FlagColors(String flagImagePath, float red, float orange, float yellow, float green_1, float green_2, float green_3, float blue_1, float blue_2, float blue_3, float indigo, float pink, float magenta, float black, float white) {
        this.flagImagePath = flagImagePath;
        this.red = red;
        this.orange = orange;
        this.yellow = yellow;
        this.green_1 = green_1;
        this.green_2 = green_2;
        this.green_3 = green_3;
        this.blue_1 = blue_1;
        this.blue_2 = blue_2;
        this.blue_3 = blue_3;
        this.indigo = indigo;
        this.pink = pink;
        this.magenta = magenta;
        this.black = black;
        this.white = white;
    }

    public float getRed() {
        return red;
    }

    public float getOrange() {
        return orange;
    }

    @Override
    public String toString() {
        return "FlagColors{" +
                "red=" + red +
                ", orange=" + orange +
                ", yellow=" + yellow +
                ", green_1=" + green_1 +
                ", green_2=" + green_2 +
                ", green_3=" + green_3 +
                ", blue_1=" + blue_1 +
                ", blue_2=" + blue_2 +
                ", blue_3=" + blue_3 +
                ", indigo=" + indigo +
                ", pink=" + pink +
                ", magenta=" + magenta +
                ", black=" + black +
                ", white=" + white +
                '}';
    }

    public float getYellow() {
        return yellow;
    }

    public float getGreen_1() {
        return green_1;
    }

    public float getGreen_2() {
        return green_2;
    }

    public float getGreen_3() {
        return green_3;
    }

    public float getBlue_1() {
        return blue_1;
    }

    public float getBlue_2() {
        return blue_2;
    }

    public float getBlue_3() {
        return blue_3;
    }

    public float getIndigo() {
        return indigo;
    }

    public float getPink() {
        return pink;
    }

    public float getMagenta() {
        return magenta;
    }

    public float getBlack() {
        return black;
    }

    public float getWhite() {
        return white;
    }
}
