package com.evandromurilo.myrpg;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.w3c.dom.Text;

public class InventoryUI {
    private Stage stage;
    private DragAndDrop dragAndDrop;
    private Table table;
    private Skin skin;
    private ItemBag bag;

    public InventoryUI(ItemBag bag) {
        this.bag = bag;

        stage = new Stage(new ScreenViewport());

        dragAndDrop = new DragAndDrop();
        table = new Table();
        table.setFillParent(true);
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
                    }
                });

                table.add(slotActor).size(64).pad(2);
            }
            table.row();
        }
        stage.addActor(table);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }
}
