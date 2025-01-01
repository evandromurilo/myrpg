package com.evandromurilo.myrpg;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
}
