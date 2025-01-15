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
            if (item.isUnique()) {
                return item.getName();
            } else {
                return String.format("%s (%d)", item.getName(), quantity);
            }
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

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void doEmpty() {
        this.quantity = 0;
        this.item = null;
    }
}
