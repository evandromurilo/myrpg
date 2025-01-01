package com.evandromurilo.myrpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public class Portal {
    private int targetX;
    private int targetY;
    private String targetMap;
    private float x;
    private float y;
    private float width;
    private float height;

    public Portal(MapObject mapObject) {
        MapProperties p = mapObject.getProperties();
        targetMap = (String) p.get("destinationMap");
        targetX = (int) p.get("targetX");
        targetY  = (int) p.get("targetY");

        x = (float) p.get("x") / 10f;
        y = (float) p.get("y") / 10f;
        width = (float) p.get("width") / 10f;
        height = (float) p.get("height") / 10f;
    }

    public boolean hit(float originX, float originY) {
        return originX >= x && originX < x + width &&
            originY >= y && originY < y + height;
    }
}
