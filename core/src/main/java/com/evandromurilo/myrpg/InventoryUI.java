package com.evandromurilo.myrpg;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class InventoryUI {
    private Stage stage;
    private DragAndDrop dragAndDrop;
    private Table root;
    private Table bagTable;
    private Table gearTable;
    private Skin skin;
    private ItemBag bag;
    private GearSet gearSet;

    public InventoryUI(ItemBag bag, GearSet gearSet) {
        this.bag = bag;

        this.gearSet = gearSet;
        stage = new Stage(new ScreenViewport());

        dragAndDrop = new DragAndDrop();
        root = new Table();
        root.setFillParent(true);

        bagTable = new Table();
        gearTable = new Table();

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Build the grid of slots
        for (int row = 0; row < bag.getHeight(); row++) {
            for (int col = 0; col < bag.getWidth(); col++) {
                final ItemSlot slot = bag.getSlots()[row][col];
                TextButton slotActor = new TextButton(slot.getName(), skin);

                slotActor.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Gdx.app.debug("Inventory", String.format("Clicked on %s", slot.getName()));

                        if (slot.getItem() != null) {
                            Item item = slot.getItem();
                            if (gearSet.equipOnEmptySlot(item)) {
                                slot.doEmpty();
                                ((TextButton) actor).setText(slot.getName());
                                refreshGearSlots();
                            };
                        }
                    }
                });

                bagTable.add(slotActor).size(64).pad(2);
            }
            bagTable.row();
        }

        refreshGearSlots();

        root.add(bagTable);
        root.add(gearTable).padLeft(10);
        stage.addActor(root);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public void refreshGearSlots() {
        gearTable.clear();
        for (GearSlot slot : gearSet.getSlots()) {
            TextButton slotActor = new TextButton(slot.getDisplayName(), skin);
            gearTable.add(slotActor).size(64).pad(2);
        }
    }
}
