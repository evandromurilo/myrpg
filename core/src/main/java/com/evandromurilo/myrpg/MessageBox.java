package com.evandromurilo.myrpg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MessageBox {
    private Stage stage;
    private Label messagesLabel;
    private Table table;

    public MessageBox() {
        stage = new Stage();

        table = new Table();
        table.setFillParent(true);
        table.left().bottom();
        stage.addActor(table);

        messagesLabel = new Label("Welcome back!",  new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(messagesLabel).padLeft(20).padBottom(20);
    }

    public void draw() {
        stage.draw();
    }
}
