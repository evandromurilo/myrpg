package com.evandromurilo.myrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.evandromurilo.myrpg.Entity;

public class MainGameScreen implements Screen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Entity player;
    private Texture peopleTexture;
    private SpriteBatch spriteBatch;

    @Override
    public void show() {
        map = new TmxMapLoader().load("overworld.tmx");

        // aqui são 1/10 porque o tileset é 10px por 10px
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 10f);
        camera = new OrthographicCamera();

        // aqui eu digo que quero 30 x 20, que vai ser convertido para a escala
        // na prática, ele vai esticar os tiles até caberem 30 na horizontal e 20 na vertical
        camera.setToOrtho(false, 30, 20);

        spriteBatch = new SpriteBatch();
        player = new Entity();

        // começa no top left, o mapa é 100x100
        player.x = 15f;
        player.y = 90f;

        peopleTexture = new Texture(Gdx.files.internal("People.png"));
        // aqui eu tenho o width e height real do sprite na spritesheet
        player.region = new TextureRegion(peopleTexture, 0, 0, 10, 10);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = player.x;
        camera.position.y = player.y;

        camera.update();
        renderer.setView(camera);
        renderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        // aqui eu tenho o width e height na escala, então 1, 1 = 10x10
        spriteBatch.draw(player.region, player.x, player.y, 1, 1);
        spriteBatch.end();
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
