package com.evandromurilo.myrpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class MessageBox {
    private Stage stage;
    private Table messagesTable;
    private ArrayList<String> messages;
    private ScrollPane messageScroller;

    public MessageBox() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        messages = new ArrayList<>();

        Table root = new Table();
        root.setFillParent(true);
        root.padBottom(10);
        root.padLeft(10);

        messagesTable = new Table();
        messageScroller = new ScrollPane(messagesTable);
        messageScroller.setScrollingDisabled(true, false);
        push("Welcome back!");

        root.add(messageScroller).maxHeight(250).bottom().left().expand().row();
        stage.addActor(root);
    }

    public void push(String message) {
        messages.add(message);
        Label text = new Label(message, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        text.setWrap(true);
        Cell<Label> newMessage = messagesTable.add(text);
        newMessage.width(300).row();
        messageScroller.scrollTo(0, 0, 0, 0);
    }

    public void clear() {
        messagesTable.clearChildren();
        messages.clear();
    }

    public void draw(float v) {
        stage.getViewport().apply();
        stage.act(v);
        stage.draw();
    }
}
