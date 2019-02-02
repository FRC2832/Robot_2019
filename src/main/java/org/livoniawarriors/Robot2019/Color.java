package org.livoniawarriors.Robot2019;

public class Color {
    private short red;
    private short green;
    private short blue;

    public static final Color RED = new Color(0, 0, 255);
    public static final Color GREEN = new Color(0, 255, 0);
    public static final Color BLUE = new Color(0, 0, 255);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color OFF = new Color(0, 0, 0);

    public Color(short red, short green, short blue) {
        if(red > 255) {
            this.red = 255;
        }
        else if(red < 0) {
            this.red = 0;
        }
        else {
            this.red = red;
        }

        if(green > 255) {
            this.green = 255;
        }
        else if(green < 0) {
            this.green = 0;
        }
        else {
            this.green = green;
        }

        if(blue > 255) {
            this.blue = 255;
        }
        else if(blue < 0) {
            this.blue = 0;
        }
        else {
            this.blue = blue;
        }
    }

    public Color(int red, int green, int blue) {
        this((short)red, (short)green, (short)blue);
    }

    public short getRed() {
        return red;
    }

    public short getGreen() {
        return green;
    }

    public short getBlue() {
        return blue;
    }
}