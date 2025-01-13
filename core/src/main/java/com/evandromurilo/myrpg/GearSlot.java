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

    public String getName() {
        return name;
    }
}
