package com.evandromurilo.myrpg;

public class ItemBag extends Item {
    private ItemSlot[][] slots;
    private int height;
    private int width;

    public ItemBag(int width, int height, String name) {
        super(name);
        this.width = width;
        this.height = height;
        slots = new ItemSlot[height][width];
        for (int i = 0; i < slots.length; i++) {
            for (int j = 0; j < slots[i].length; j++) {
                slots[i][j] = new ItemSlot();
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ItemSlot[][] getSlots() {
        return slots;
    }
}
