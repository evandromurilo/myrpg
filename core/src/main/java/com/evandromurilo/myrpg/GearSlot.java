package com.evandromurilo.myrpg;

import java.util.ArrayList;

public class GearSlot implements ItemHolder {
    private ItemType[] allowedTypes;
    private Item item;
    private String name;
    private boolean visible;

    public GearSlot(String name, ItemType... types) {
        this.name = name;
        allowedTypes = types;
        this.visible = true;
    }

    public boolean isEmpty() {
        return this.item == null;
    }

    public boolean isAllowed(ItemType type) {
        for (ItemType allowedtype : allowedTypes) {
            if (type == allowedtype) {
                return true;
            }
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        if (this.item != null) {
            return this.item.getName();
        } else {
            return this.getName();
        }
    }

    public void doEmpty() {
        this.item = null;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isVisible() {
        return visible;
    }

    public void hide() {
        visible = false;
    }

    public void show() {
        visible = true;
    }
}
