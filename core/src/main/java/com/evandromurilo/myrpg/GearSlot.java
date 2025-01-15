package com.evandromurilo.myrpg;

import java.util.ArrayList;

public class GearSlot {
    private ItemType[] allowedTypes;
    private Item item;
    private String name;

    public GearSlot(String name, ItemType... types) {
        this.name = name;
        allowedTypes = types;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
