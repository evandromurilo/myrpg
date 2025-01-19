package com.evandromurilo.myrpg;

public class ItemSlot implements ItemHolder {
    private Item item;
    private int quantity;
    private boolean visible;

    public ItemSlot() {
        this.item = null;
        this.quantity = 0;
        this.visible = true;
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

    public boolean isVisible() {
        return visible;
    }

    public void hide() {
        visible = false;
    }

    public void show() {
        visible = true;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isEmpty() {
        return item == null;
    }
}
