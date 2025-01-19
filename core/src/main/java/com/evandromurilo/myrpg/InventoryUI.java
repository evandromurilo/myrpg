package com.evandromurilo.myrpg;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

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
    private MouseState mouseState;
    private float mouseStateTime = 0;
    private ArrayList<InventorySlot> slotList = new ArrayList<>();

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

        slotList.clear();

        for (int row = 0; row < bag.getHeight(); row++) {
            for (int col = 0; col < bag.getWidth(); col++) {
                final ItemSlot slot = bag.getSlots()[row][col];

                // Desenha o fundo do slot
                batch.draw(slotRegion, x, y, SLOT_WIDTH, SLOT_HEIGHT);
                slotList.add(new InventorySlot(slot, x, y, SLOT_WIDTH, SLOT_HEIGHT));

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
            slotList.add(new InventorySlot(slot, x, y, SLOT_WIDTH, SLOT_HEIGHT));

            font.draw(batch, slot.getDisplayName(), x + 5, y + 35);

            x += SLOT_WIDTH + MARGIN;
        }
    }

    private void changeMouseState(MouseState newState) {
        mouseState = newState;
        mouseStateTime = 0;

        int mx = Gdx.input.getX();
        int my = Gdx.graphics.getHeight() - Gdx.input.getY();

        Gdx.app.debug("Mouse", String.format("%s(%d, %d)", newState.name(), mx, my));

        if (newState == MouseState.DOUBLE_CLICKED) {
            checkDoubleClick(mx, my);
        } else if (newState == MouseState.HOLDING) {
            checkHold(mx, my);
        }
    }

    public void update(float v) {
        mouseStateTime += v;

        if (!Gdx.input.isTouched() && mouseStateTime > 0.3 && mouseState != MouseState.RELEASED) {
            changeMouseState(MouseState.RELEASED);
        } else if (mouseState == MouseState.RELEASED) {
            if (Gdx.input.isTouched()) {
                changeMouseState(MouseState.CLICKING);
            }
        } else if (mouseState == MouseState.CLICKING) {
            if (!Gdx.input.isTouched()) {
                changeMouseState(MouseState.CLICKED);
            } else if (mouseStateTime > 0.3) {
                changeMouseState(MouseState.HOLDING);
            }
        } else if (mouseState == MouseState.CLICKED) {
            if (Gdx.input.justTouched()) {
                changeMouseState(MouseState.DOUBLE_CLICKING);
            }
        } else if (mouseState == MouseState.DOUBLE_CLICKING) {
            if (!Gdx.input.isTouched()) {
                changeMouseState(MouseState.DOUBLE_CLICKED);
            }
        }
    }

    public void checkHold(int mx, int my) {

    }

    public ItemHolder getSlotAtPoint(int mx, int my) {
        for (InventorySlot slot : slotList) {
            if (slot.containsPoint(mx, my)) {
                return slot.getHolder();
            }
        }

        return null;
    }

    public void checkDoubleClick(int mx, int my) {
       ItemHolder slot = getSlotAtPoint(mx, my);

        if (slot instanceof GearSlot holder) {
            Gdx.app.debug("Gear", String.format("Hit %s", holder.getDisplayName()));

            Item item = holder.getItem();
            if (item != null) {
                ItemSlot bagSlot = bag.findSuitableSlot(item);

                if (bagSlot != null) {
                    bagSlot.setItem(item);
                    bagSlot.addQuantity(1);

                    holder.doEmpty();
                }
            }
        } else if (slot instanceof ItemSlot holder) {
            Gdx.app.debug("Inventory", String.format("Hit %s", holder.getName()));

            if (holder.getItem() != null) {
                Item item = holder.getItem();
                if (gearSet.equipOnEmptySlot(item)) {
                    holder.doEmpty();
                };
            }
        }
    }
}
