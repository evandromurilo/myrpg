package com.evandromurilo.myrpg;

import java.util.ArrayList;

public class Item {
    private String name;
    private ItemType type;
    private ArrayList<StatusModifier> modifiers = new ArrayList<>();

    public Item(String name, ItemType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return this.type == ItemType.WEAPON_GEAR ||
            this.type == ItemType.BODY_GEAR ||
            this.type == ItemType.BAG;
    }

    public ItemType getType() {
        return type;
    }

    public void addModifier(StatusType statusType, float modifier) {
        modifiers.add(new StatusModifier(statusType, modifier));
    }

    public ArrayList<StatusModifier> getModifiers() {
        return modifiers;
    }
}
