package com.evandromurilo.myrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainGameScreen implements Screen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    @Override
    public void show() {
        map = new TmxMapLoader().load("overworld.tmx");

        // aqui são 1/10 porque o tileset é 10px por 10px
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 10f);
        camera = new OrthographicCamera();

        // aqui eu digo que quero 30 x 20, que vai ser convertido para a escala
        // na prática, ele vai esticar os tiles até caberem 30 na horizontal e 20 na vertical
        camera.setToOrtho(false, 30, 20);

        // começa no top left, o mapa é 100x100
        camera.position.x = 15;
        camera.position.y = 90;
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
