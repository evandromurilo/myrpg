package com.evandromurilo.myrpg;

public class Item {
    private String name;
    private ItemType type;

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
}
