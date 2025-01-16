package com.evandromurilo.myrpg;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class InventoryUI {
    private ItemBag bag;
    private GearSet gearSet;
    private Texture inventoryTexture;
    private TextureRegion slotRegion;
    private BitmapFont font;

    public InventoryUI(ItemBag bag, GearSet gearSet) {
        this.bag = bag;
        this.gearSet = gearSet;

        inventoryTexture = new Texture(Gdx.files.internal("Inventory.png"));
        slotRegion = new TextureRegion(inventoryTexture, 0, 0, 10, 10);
        font = new BitmapFont(); // ou pode carregar um .fnt
    }

    public void render(float delta, SpriteBatch batch) {
        float startX = 20;
        float startY = 200;
        float slotWidth = 60;
        float slotHeight = 60;

        for (int row = 0; row < bag.getHeight(); row++) {
            for (int col = 0; col < bag.getWidth(); col++) {
                final ItemSlot slot = bag.getSlots()[row][col];

                float x = startX + col * slotWidth;
                float y = startY + (bag.getWidth() - row + 1) * slotHeight;

                // Desenha o fundo do slot
                batch.draw(slotRegion, x, y, slotWidth, slotHeight);

                // Desenha o item, se existir
                if (slot.getItem() != null) {
                    //batch.draw(slot.getItem().getTexture(), x, y, slotWidth, slotHeight);

                    // Desenha texto (nome ou quantidade)
                    font.draw(batch, slot.getName(), x + 5, y + 35);
                }
            }
        }

        float x = 200;
        float y = 300;
        for (GearSlot slot : gearSet.getSlots()) {
            batch.draw(slotRegion, x, y, slotWidth, slotHeight);
            font.draw(batch, slot.getDisplayName(), x + 5, y + 35);
            x += slotWidth;
        }
    }

    public void refreshBagSlots() {
//        if (slot.getItem() != null) {
//            Item item = slot.getItem();
//            if (gearSet.equipOnEmptySlot(item)) {
//                slot.doEmpty();
//                ((TextButton) actor).setText(slot.getName());
//                refreshGearSlots();
//            };
//        }
    }

    public void refreshGearSlots() {
//        if (slot.getItem() != null) {
//            Item item = slot.getItem();
//            ItemSlot bagSlot = bag.findSuitableSlot(item);
//
//            if (bagSlot != null) {
//                bagSlot.setItem(item);
//                bagSlot.addQuantity(1);
//
//                slot.doEmpty();
//                refreshGearSlots();
//                refreshBagSlots();
//            }
//        }
    }
}
