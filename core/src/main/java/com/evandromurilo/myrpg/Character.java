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
    private Character target;
    private float totalTime = 0.10f;
    private float currentTime;
    private int hp = 10;
    private CharacterType type;

    public Character(MapObject obj, Texture peopleTexture) {
        MapProperties p = obj.getProperties();
        if (p.get("type").equals("merchant")) {
            type = CharacterType.MERCHANT;
            region = new TextureRegion(peopleTexture, 0, 20, 10, 10);
        } else {
            region = new TextureRegion(peopleTexture, 0, 0, 10, 10);
        }

        teleport((float) p.get("x") / 10f, (float) p.get("y") / 10f);
    }

    public Character(CharacterType type) {
        this.type = type;
    }

    public void update(float v, Level level) {
        if (state == CharacterState.IDLE && (targetY != y || targetX != x)) {
            // for now assuming 1 step increments
            Character character = level.characterAt(targetX, targetY);
            if (character != null) {
                if (character.getType() == CharacterType.MERCHANT) {
                    level.echo("Greetings, adventurer!");
                    state = CharacterState.TALKING;
                    currentTime = 0;
                    // target = character;
                } else if (character.getType() == CharacterType.MONSTER) {
                    state = CharacterState.ATTACKING;
                    target = character;
                    currentTime = 0;
                }
            } else if (canWalk(level, targetX, targetY)) {
                startMove();
            }
        }

        if (state != CharacterState.IDLE) {
            currentTime += v;
        }

        if (state == CharacterState.WALKING) {
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
        } else if (state == CharacterState.TALKING) {
            if (currentTime >= totalTime) {
                clearTarget();
                state = CharacterState.IDLE;
            }
        } else if (state == CharacterState.ATTACKING) {
            if (currentTime >= totalTime) {
                doAttack(level);
            }
        }
    }

    public CharacterType getType() {
        return type;
    }

    public void doAttack(Level level) {
        int damage = target.receiveHit(2);
        level.echo(String.format("You hit for %d damage", damage));
        if (target.getState() == CharacterState.DEAD) {
            level.echo("You kill the enemy!");
            target = null;
        }
        state = CharacterState.IDLE;
        clearTarget();
    }

    public int receiveHit(int strength) {
        hp -= strength;
        if (hp <= 0) {
            state = CharacterState.DEAD;
            return strength+hp;
        } else {
            return strength;
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

    public void setTarget(float dx, float dy) {
        targetX = x+dx;
        targetY = y+dy;
    }

    public void clearTarget() {
        targetX = x;
        targetY = y;
        target = null;
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

    public Character getTarget() {
        return target;
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
