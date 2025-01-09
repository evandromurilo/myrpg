package com.evandromurilo.myrpg;

public class ItemSlot {
    private Item item;
    private int quantity;

    public ItemSlot() {
        this.item = null;
        this.quantity = 0;
    }

    public ItemSlot(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}
