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

    public String getName()
    {
        if (item != null) {
            return String.format("%s (%d)", item.getName(), quantity);
        } else {
            return "Empty";
        }
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void addQuantity() {
        this.quantity++;
    }
}
