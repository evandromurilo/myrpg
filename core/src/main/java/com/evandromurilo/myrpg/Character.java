package com.evandromurilo.myrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Character {
    private float x = 0f;
    private float y = 0f;
    public TextureRegion region;
    private boolean still = true;
    private float startX;
    private float startY;
    private float targetY;
    private float targetX;
    private float totalTime = 0.10f;
    private float currentTime;

    public Character(MapObject obj, Texture peopleTexture) {
        MapProperties p = obj.getProperties();
        region = new TextureRegion(peopleTexture, 0, 0, 10, 10);
        teleport((float) p.get("x") / 10f, (float) p.get("y") / 10f);
    }

    public Character() {

    }

    public void update(float v, TiledMap map) {
        if (still && (targetY != y || targetX != x) && canWalk(map, targetX, targetY)) {
            startMove();
        }

        if (!still) {
            currentTime += v;

            if (currentTime >= totalTime) {
                x = targetX;
                y = targetY;
                still = true;
            } else {
                x = startX+deltaMovement(startX, targetX);
                y = startY+deltaMovement(startY, targetY);
            }
        }
    }

    public void startMove()
    {
        still = false;
        startX = x;
        startY = y;
        currentTime = 0f;
    }

    public boolean canWalk(TiledMap map, float x, float y)
    {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Collision");
        if (layer.getCell((int) x, (int) y) != null) { // cannot have collision tile
            return false;
        }

        TiledMapTileLayer base = (TiledMapTileLayer) map.getLayers().get("Base");
        return base.getCell((int) x, (int) y) != null; // must hava base tile
    }

    public void setMoveTarget(float dx, float dy) {
        targetX = x+dx;
        targetY = y+dy;
    }

    public boolean isStill() {
        return still;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void teleport(float tx, float ty)
    {
        still = true;
        x = tx;
        y = ty;
        targetX = x;
        targetY = y;
    }

    private float deltaMovement(float start, float target) {
        return (currentTime/totalTime) * (target-start);
    }
}
