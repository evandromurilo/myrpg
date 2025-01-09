package com.evandromurilo.myrpg;

public class ItemBag extends Item {
    private ItemSlot[][] slots;
    private int height;
    private int width;

    public ItemBag(int width, int height, String name) {
        super(name);
        slots = new ItemSlot[height][width];
        for (int i = 0; i < slots.length; i++) {
            for (int j = 0; j < slots[i].length; j++) {
                slots[i][j] = new ItemSlot();
            }
        }
    }
}
