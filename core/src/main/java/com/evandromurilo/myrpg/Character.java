package com.evandromurilo.myrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

import java.util.IdentityHashMap;
import java.util.Map;

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
    private float totalTime = 0.13f;
    private float currentTime;
    private int hp = 10;
    private int speed = 10;
    private CharacterType type;
    private IdentityHashMap<Character, Alignment> alignmentMap = new IdentityHashMap<>();
    private ItemBag itemBag;
    private GearSet gearSet;

    public Character(MapObject obj, Texture peopleTexture) {
        MapProperties p = obj.getProperties();
        if (p.get("type").equals("merchant")) {
            type = CharacterType.MERCHANT;
            region = new TextureRegion(peopleTexture, 0, 20, 10, 10);
        } else if (p.get("type").equals("giver")) {
            type = CharacterType.GIVER;
            region = new TextureRegion(peopleTexture, 0, 240, 10, 10);
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
                } else if (character.getType() == CharacterType.GIVER) {
                    level.echo("Here, take this!");
                    receiveItem(new Item("Gold", ItemType.GOLD), 4);
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
                    endTurn();
                }
            } else {
                x = startX+deltaMovement(startX, targetX);
                y = startY+deltaMovement(startY, targetY);
            }
        } else if (state == CharacterState.TALKING) {
            if (currentTime >= totalTime) {
                clearTarget();
                endTurn();
            }
        } else if (state == CharacterState.ATTACKING) {
            if (currentTime >= totalTime) {
                doAttack(level);
            }
        }
    }

    public void receiveItem(Item item) {
        if (itemBag != null) {
            itemBag.store(item);
        }
    }

    public void receiveItem(Item item, int quantity) {
        if (itemBag != null) {
            itemBag.store(item, quantity);
        }
    }

    public CharacterType getType() {
        return type;
    }

    public float getStatusValue(StatusType statusType) {
        float total = 0f;

        if (gearSet == null) {
            return total;
        }

        for (Item item : gearSet.getItems()) {
            for (StatusModifier modifier : item.getModifiers()) {
                if (modifier.getStatusType() == statusType) {
                    total += modifier.getModifier();
                }
            }
        }
        return total;
    }

    public void doAttack(Level level) {
        int damage = target.receiveHit((int) getStatusValue(StatusType.ATTACK) + 2, this);

        if (type == CharacterType.PLAYER) {
            level.echo(String.format("You hit %s for %d damage", target.getType().toString(), damage));
        } else {
            level.echo(String.format("%s hits %s for %d damage", type.toString(), target.getType().toString(), damage));
        }

        if (target.getState() == CharacterState.DEAD) {
            if (type == CharacterType.PLAYER) {
                level.echo("You kill the enemy!");
            } else {
                level.echo(String.format("You were killed by %s!", type.toString()));
            }

            target = null;
        }
        endTurn();
        clearTarget();
    }

    public int receiveHit(int strength, Character attacker) {
        hp -= strength;
        if (hp <= 0) {
            state = CharacterState.DEAD;
            return strength+hp;
        } else {
            if (type == CharacterType.MONSTER) {
                // simple monster AI for now: attack when attacked
                setAlignment(attacker, Alignment.ENEMY);
            }
            return strength;
        }
    }

    public void setAlignment(Character target, Alignment alignment) {
        alignmentMap.put(target, alignment);
    }

    public Alignment getAligment(Character target) {
        return alignmentMap.getOrDefault(target, Alignment.NEUTRAL);
    }

    public void startMove()
    {
        state = CharacterState.WALKING;
        startX = x;
        startY = y;
        currentTime = 0f;
    }

    public boolean attemptMoveTowards(Character target, Level level) {
        float dy = target.getY() - y;
        float dx = target.getX() - x;

        if (Math.abs(dy) > Math.abs(dx) && Math.abs(dy) >= 1) {
            targetX = x;
            targetY = y + (dy > 0 ? 1 : -1);
        } else if (Math.abs(dx) >= 1) {
            targetY = y;
            targetX = x + (dx > 0 ? 1 : -1);
        }

        if (canWalk(level, targetX, targetY)) {
            startMove();
            return true;
        } else {
            return false;
        }
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

    public int getSpeed() {
        return speed;
    }

    private float deltaMovement(float start, float target) {
        return (currentTime/totalTime) * (target-start);
    }

    public void setState(CharacterState characterState) {
        state = characterState;
    }

    public boolean touches(Character target) {
        return (Math.abs(target.getX() - x) + Math.abs(target.getY() - y)) <= 1;
    }

    public void endTurn() {
        state = CharacterState.FINISHED_ACTION;
    }

    public void chooseAction(Level level) {
        if (type == CharacterType.MONSTER) {
            for (Map.Entry<Character, Alignment> entry : alignmentMap.entrySet()) {
                // attack first ENEMY (should be only player for now)
                if (entry.getValue() == Alignment.ENEMY) {
                    if (touches(entry.getKey())) {
                        state = CharacterState.ATTACKING;
                        target = entry.getKey();
                        currentTime = 0;

                    } else {
                        if (!attemptMoveTowards(entry.getKey(), level)) {
                            endTurn(); // for now npcs do nothing
                        }
                    }

                    return;
                }
            }
        }

        endTurn(); // for now npcs do nothing
    }

    public void setItemBag(ItemBag itemBag) {
        this.itemBag = itemBag;
    }

    public ItemBag getItemBag() {
        return itemBag;
    }

    public GearSet getGearSet() {
        return gearSet;
    }

    public void setGearSet(GearSet gearSet) {
        this.gearSet = gearSet;
    }
}
