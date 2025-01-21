package com.evandromurilo.myrpg;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    private final TextureRegion defaultItemRegion;
    private final Sound sound;
    private final Sound sound2;
    private ItemBag bag;
    private GearSet gearSet;
    private Texture inventoryTexture;
    private TextureRegion slotRegion;
    private BitmapFont font;
    private MouseState mouseState;
    private float mouseStateTime = 0;
    private ArrayList<InventorySlot> slotList = new ArrayList<>();
    private ItemHolder dragging;
    int mouseStateStartX;
    int mouseStateStartY;

    public InventoryUI(ItemBag bag, GearSet gearSet) {
        this.bag = bag;
        this.gearSet = gearSet;

        sound = Gdx.audio.newSound(Gdx.files.internal("02.wav"));
        sound2 = Gdx.audio.newSound(Gdx.files.internal("10.wav"));

        inventoryTexture = new Texture(Gdx.files.internal("Inventory.png"));
        defaultItemRegion = new TextureRegion(inventoryTexture, 10, 0, 10, 10);
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
                    // Desenha texto (nome ou quantidade)
                    if (slot.isVisible()) {
                        batch.draw(defaultItemRegion, x, y, SLOT_WIDTH, SLOT_HEIGHT);
                        font.draw(batch, slot.getName(), x + 5, y + 35);
                    }
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

            if (slot.isVisible()) {
                if (!slot.isEmpty()) {
                    batch.draw(defaultItemRegion, x, y, SLOT_WIDTH, SLOT_HEIGHT);
                }

                font.draw(batch, slot.getDisplayName(), x + 5, y + 35);
            }

            x += SLOT_WIDTH + MARGIN;
        }

        if (dragging != null) {
            int mx = Gdx.input.getX();
            int my = Gdx.graphics.getHeight() - Gdx.input.getY();

            batch.draw(defaultItemRegion, mx - SLOT_WIDTH/2, my - SLOT_HEIGHT/2, SLOT_WIDTH, SLOT_HEIGHT);
        }
    }

    private void changeMouseState(MouseState newState) {
        MouseState oldState = mouseState;
        mouseState = newState;
        mouseStateTime = 0;
        mouseStateStartY = Gdx.input.getY();
        mouseStateStartX = Gdx.input.getX();

        int mx = Gdx.input.getX();
        int my = Gdx.graphics.getHeight() - Gdx.input.getY();

        Gdx.app.debug("Mouse", String.format("%s(%d, %d)", newState.name(), mx, my));

        if (newState == MouseState.DOUBLE_CLICKED) {
            checkDoubleClick(mx, my);
        } else if (newState == MouseState.HOLDING) {
            checkHold(mx, my);
        } else if (newState == MouseState.RELEASED) {
            checkRelease(mx, my);
        }
    }

    public void update(float v) {
        mouseStateTime += v;

        if (!Gdx.input.isTouched() && mouseStateTime > 0.3 && mouseState != MouseState.RELEASED) {
            changeMouseState(MouseState.RELEASED);
        } else if (!Gdx.input.isTouched() && mouseState == MouseState.HOLDING) {
            changeMouseState(MouseState.RELEASED);
        } else if (mouseState == MouseState.RELEASED) {
            if (Gdx.input.isTouched()) {
                changeMouseState(MouseState.CLICKING);
            }
        } else if (mouseState == MouseState.CLICKING) {
            if (!Gdx.input.isTouched()) {
                changeMouseState(MouseState.CLICKED);
            } else if (Math.abs(mouseStateStartX - Gdx.input.getX()) > 3 || Math.abs(mouseStateStartY - Gdx.input.getY()) > 3) {
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
        ItemHolder holder = getSlotAtPoint(mx, my);

        if (holder != null && !holder.isEmpty()) {
            dragging = holder;
            holder.hide();
        }
    }

    public void checkRelease(int mx, int my) {
        if (dragging == null) {
            return;
        }

        ItemHolder holder = getSlotAtPoint(mx, my);

        if (holder instanceof ItemSlot holderSlot && dragging instanceof ItemSlot draggingSlot) {
            sound2.play();
            Item holderItem = holderSlot.getItem();
            int holderQuantity = holderSlot.getQuantity();

            holderSlot.setItem(draggingSlot.getItem());
            holderSlot.setQuantity(draggingSlot.getQuantity());

            draggingSlot.setItem(holderItem);
            draggingSlot.setQuantity(holderQuantity);
        } else if (holder instanceof ItemSlot holderSlot && dragging instanceof GearSlot draggingSlot) {
            if (holder.isEmpty()) {
                sound2.play();
                holderSlot.setItem(draggingSlot.getItem());
                holderSlot.setQuantity(1);
                draggingSlot.doEmpty();
            }
        } else if (holder instanceof GearSlot holderSlot && dragging instanceof ItemSlot draggingSlot) {
            if (holderSlot.isAllowed(draggingSlot.getItem().getType())) {
                if (holderSlot.isEmpty()) {
                    sound2.play();
                    holderSlot.setItem(draggingSlot.getItem());
                    draggingSlot.doEmpty();
                } else {
                    sound2.play();
                    Item draggingItem = draggingSlot.getItem();

                    draggingSlot.setItem(holderSlot.getItem());
                    draggingSlot.setQuantity(1);

                    holderSlot.setItem(draggingItem);
                }
            }
        }

        if (dragging != null) {
            dragging.show();
        }

        dragging = null;
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
                    sound.play();
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
                    sound.play();
                    holder.doEmpty();
                };
            }
        }
    }
}
