package com.evandromurilo.myrpg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.evandromurilo.myrpg.screens.MainGameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private MainGameScreen mainGameScreen;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        mainGameScreen = new MainGameScreen();
        setScreen(mainGameScreen);
    }
    @Override
    public void dispose() {
        mainGameScreen.dispose();
    }
}
