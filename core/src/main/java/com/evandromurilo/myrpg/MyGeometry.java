package com.evandromurilo.myrpg;

public class MyGeometry {
    /**
     * @param mx Point x
     * @param my Point y
     * @param x left of rectangle
     * @param y bottom of rectangle
     * @param width width of rectangle
     * @param height height of rectangle
     */
    public static boolean isPointInRectangle(int mx, int my, float x, float y, float width, float height) {
        return mx >= x &&
            mx <= x+width &&
            my >= y &&
            my <= y+height;
    }
}
