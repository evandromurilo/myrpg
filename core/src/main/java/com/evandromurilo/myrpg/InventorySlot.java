package com.evandromurilo.myrpg;

public class InventorySlot {
    private ItemHolder holder;
    private float x;
    private float y;
    private float width;
    private float height;

    public InventorySlot(ItemHolder slot, float x, float y, float width, float height) {
        this.holder = slot;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean containsPoint(int mx, int my) {
        return MyGeometry.isPointInRectangle(mx, my, x, y, width, height);
    }

    public ItemHolder getHolder() {
        return holder;
    }
}
