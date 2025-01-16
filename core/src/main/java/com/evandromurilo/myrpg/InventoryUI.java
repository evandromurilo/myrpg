package com.evandromurilo.myrpg;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class InventoryUI {
    public final float START_X = 20;
    public final float START_Y = 400;
    private final float SLOT_WIDTH = 60;
    private final float SLOT_HEIGHT = 60;
    private final float MARGIN = 5;
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
        float x = START_X;
        float y = START_Y;

        for (int row = 0; row < bag.getHeight(); row++) {
            for (int col = 0; col < bag.getWidth(); col++) {
                final ItemSlot slot = bag.getSlots()[row][col];

                // Desenha o fundo do slot
                batch.draw(slotRegion, x, y, SLOT_WIDTH, SLOT_HEIGHT);

                // Desenha o item, se existir
                if (slot.getItem() != null) {
                    //batch.draw(slot.getItem().getTexture(), x, y, slotWidth, slotHeight);

                    // Desenha texto (nome ou quantidade)
                    font.draw(batch, slot.getName(), x + 5, y + 35);
                }

                x += SLOT_WIDTH + MARGIN;
            }
            x = START_X;
            y -= SLOT_HEIGHT + MARGIN;
        }

        y = START_Y + 100;
        for (GearSlot slot : gearSet.getSlots()) {
            batch.draw(slotRegion, x, y, SLOT_WIDTH, SLOT_HEIGHT);
            font.draw(batch, slot.getDisplayName(), x + 5, y + 35);
            x += SLOT_WIDTH + MARGIN;
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

    public void checkClick(int mx, int my) {
        float x = START_X;
        float y = START_Y;

        for (int row = 0; row < bag.getHeight(); row++) {
            for (int col = 0; col < bag.getWidth(); col++) {
                final ItemSlot slot = bag.getSlots()[row][col];

                if (isPointInRectangle(mx, my, x, y, SLOT_WIDTH, SLOT_HEIGHT)) {
                    Gdx.app.debug("Inventory", String.format("Hit %s", slot.getName()));
                }

                x += SLOT_WIDTH + MARGIN;
            }

            x = START_X;
            y -= SLOT_HEIGHT + MARGIN;
        }
    }

    /**
     *
     * @param mx Point x
     * @param my Point y
     * @param x left of rectangle
     * @param y bottom of rectangle
     * @param width width of rectangle
     * @param height height of rectangle
     */
    private boolean isPointInRectangle(int mx, int my, float x, float y, float width, float height) {
        return mx >= x &&
            mx <= x+width &&
            my >= y &&
            my <= y+height;
    }
}
