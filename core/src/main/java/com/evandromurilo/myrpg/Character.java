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
    private CharacterState state = CharacterState.IDLE;
    private float startX;
    private float startY;
    private float targetY;
    private float targetX;
    private float totalTime = 0.10f;
    private float currentTime;

    public Character(MapObject obj, Texture peopleTexture) {
        MapProperties p = obj.getProperties();
        if (p.get("type").equals("merchant")) {
            region = new TextureRegion(peopleTexture, 0, 20, 10, 10);
        } else {
            region = new TextureRegion(peopleTexture, 0, 0, 10, 10);
        }

        teleport((float) p.get("x") / 10f, (float) p.get("y") / 10f);
    }

    public Character() {

    }

    public void update(float v, Level level) {
        if (state == CharacterState.IDLE && (targetY != y || targetX != x) && canWalk(level, targetX, targetY)) {
            startMove();
        }

        if (state == CharacterState.WALKING) {
            currentTime += v;

            if (currentTime >= totalTime) {
                x = targetX;
                y = targetY;

                if (level.portalAt(x, y) != null) {
                    state = CharacterState.ON_PORTAL;
                } else {
                    state = CharacterState.IDLE;
                }
            } else {
                x = startX+deltaMovement(startX, targetX);
                y = startY+deltaMovement(startY, targetY);
            }
        }
    }

    public void startMove()
    {
        state = CharacterState.WALKING;
        startX = x;
        startY = y;
        currentTime = 0f;
    }

    public boolean canWalk(Level level, float x, float y)
    {
        if (level.hasCollision(x, y)) {
            return false;
        }

        if (level.hasCharacter(x, y)) {
            return false;
        }

        return level.hasBaseTile(x, y);
    }

    public void setMoveTarget(float dx, float dy) {
        targetX = x+dx;
        targetY = y+dy;
    }

    public CharacterState getState() {
        return state;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void teleport(float tx, float ty)
    {
        state = CharacterState.IDLE;
        x = tx;
        y = ty;
        targetX = x;
        targetY = y;
    }

    private float deltaMovement(float start, float target) {
        return (currentTime/totalTime) * (target-start);
    }
}
