package com.evandromurilo.myrpg;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Entity {
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
    private TiledMap map;

    public void update(float v) {
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

    public boolean move(float dx, float dy)
    {
        if (still) {
            if (!canWalk(x+dx, y+dy)) {
                return false;
            }

            still = false;
            startX = x;
            startY = y;
            targetX = x+dx;
            targetY = y+dy;
            currentTime = 0f;
            return true;
        } else {
            return false;
        }
    }

    public boolean canWalk(float x, float y)
    {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Collision");
        if (layer.getCell((int) x, (int) y) != null) { // cannot have collision tile
            return false;
        }

        TiledMapTileLayer base = (TiledMapTileLayer) map.getLayers().get("Base");
        return base.getCell((int) x, (int) y) != null; // must hava base tile
    }

    public boolean moveUp()
    {
        return move(0, 1);
    }

    public boolean moveDown()
    {
        return move(0, -1);
    }

    public boolean moveLeft()
    {
        return move(-1, 0);
    }

    public boolean moveRight()
    {
        return move(1, 0);
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
    }

    private float deltaMovement(float start, float target) {
        return (currentTime/totalTime) * (target-start);
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }
}
