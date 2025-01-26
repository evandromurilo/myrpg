package com.evandromurilo.myrpg;

import java.util.ArrayList;

public class GearSet {
    private ArrayList<GearSlot> slots;

    public GearSet() {
        slots = new ArrayList<>();
    }

    public void addSlot(GearSlot slot) {
        slots.add(slot);
    }

    public ArrayList<GearSlot> getSlots() {
        return slots;
    }

    public boolean equipOnEmptySlot(Item item) {
        for (GearSlot slot : slots) {
            if (slot.isEmpty() && slot.isAllowed(item.getType())) {
                slot.setItem(item);
                return true;
            }
        }

        return false;
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();

        for (GearSlot slot : slots) {
            if (slot.getItem() != null) {
                items.add(slot.getItem());
            }
        }

        return items;
    }
}
