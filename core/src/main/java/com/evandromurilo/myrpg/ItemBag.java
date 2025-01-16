package com.evandromurilo.myrpg;

public class ItemBag extends Item {
    private ItemSlot[][] slots;
    private int height;
    private int width;

    public ItemBag(int width, int height, String name) {
        super(name, ItemType.BAG);
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

    public ItemSlot findSuitableSlot(Item item) {
        for (ItemSlot[] itemSlots : slots) {
            // first attempt to find an equivalent item
            for (ItemSlot slot : itemSlots) {
                if (slot.getItem() != null && !slot.getItem().isUnique() && slot.getItem().getName().equals(item.getName())) {
                    return slot;
                }
            }
        }

        for (ItemSlot[] itemSlots : slots) {
            // then use an empty slot if available
            for (ItemSlot slot : itemSlots) {
                if (slot.getItem() == null) {
                    return slot;
                }
            }
        }

        return null;
    }

    public void store(Item item) {
        ItemSlot slot = findSuitableSlot(item);
        slot.setItem(item);
        slot.addQuantity(1);
    }

    public void store(Item item, int quantity) {
        ItemSlot slot = findSuitableSlot(item);
        slot.setItem(item);
        slot.addQuantity(quantity);
    }
}
