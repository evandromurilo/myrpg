package com.evandromurilo.myrpg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

public class MessageBox {
    private Stage stage;
    private Label messagesLabel;
    private Table table;
    private ArrayList<String> messages;

    public MessageBox() {
        stage = new Stage();

        table = new Table();
        table.setFillParent(true);
        table.left().bottom();
        stage.addActor(table);

        messagesLabel = new Label("",  new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(messagesLabel).padLeft(20).padBottom(20);

        messages = new ArrayList<>();
        push("Welcome back!");
    }

    public void push(String message) {
        messages.add(message);
    }

    public void draw() {
        String content = "";
        for (String message : messages) {
            if (!content.isEmpty()) {
                content = content.concat("\n");
            }
            content = content.concat(message);
        }
        messagesLabel.setText(content);
        stage.draw();
    }
}
