package com.evandromurilo.myrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.evandromurilo.myrpg.Character;
import com.evandromurilo.myrpg.Portal;

import java.util.ArrayList;

public class MainGameScreen implements Screen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Character player;
    private Texture peopleTexture;
    private SpriteBatch spriteBatch;
    private ArrayList<Portal> portals;
    private ArrayList<Character> npcs;


    @Override
    public void show() {
        player = new Character();

        loadMap("overworld.tmx");

        camera = new OrthographicCamera();

        // aqui eu digo que quero 30 x 20, que vai ser convertido para a escala
        // na prática, ele vai esticar os tiles até caberem 30 na horizontal e 20 na vertical
        camera.setToOrtho(false, 30, 20);

        spriteBatch = new SpriteBatch();

        // começa no top left, o mapa é 100x100
        player.teleport(15f, 90f);

        peopleTexture = new Texture(Gdx.files.internal("People.png"));
        // aqui eu tenho o width e height real do sprite na spritesheet
        player.region = new TextureRegion(peopleTexture, 0, 0, 10, 10);
    }

    public void loadMap(String mapName) {
        map = new TmxMapLoader().load(mapName);

        if (renderer != null) {
            renderer.dispose();
        }

        // aqui são 1/10 porque o tileset é 10px por 10px
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 10f);

        portals = new ArrayList<>();
        MapLayer portalLayer = map.getLayers().get("Portals");
        for (MapObject obj : portalLayer.getObjects()) {
            portals.add(new Portal(obj));
        }

        npcs = new ArrayList<>();
        MapLayer npcLayer = map.getLayers().get("NPC");
        for (MapObject obj : npcLayer.getObjects()) {
            Character npc = new Character(obj, peopleTexture);
            npcs.add(npc);
        }
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.update(v, map);

        for (Character npc : npcs) {
            npc.update(v, map);
        }

        // kinda awkward, as I need to check only once, when the movement has stopped
        if (player.isStill()) {
            Portal portal = portalAt(player.getX(), player.getY());
            if (portal != null) {
                Gdx.app.debug("Map", String.format("Portal hit for %s", portal.getTargetMap()));
                loadMap(portal.getTargetMap());
                float targetY = portal.getTargetY();
                // flip y, porque salvamos as coordenadas do jeito que o tiled apresenta
                // nos outros lugares a framework faz o flip sozinha, com essa mesma conta inclusive
                targetY = (int) map.getProperties().get("height") - targetY - 1;
                player.teleport(portal.getTargetX(), targetY);
            }
        }

        if (player.isStill()) {
            if (Gdx.input.isKeyPressed(Input.Keys.J)) {
                player.setMoveTarget(0, -1);
            } else if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                player.setMoveTarget(0, 1);
            } else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                player.setMoveTarget(-1, 0);
            } else if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                player.setMoveTarget(1, 0);
            }
        }


        camera.position.x = player.getX();
        camera.position.y = player.getY();

        camera.update();
        renderer.setView(camera);
        renderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        // aqui eu tenho o width e height na escala, então 1, 1 = 10x10
        spriteBatch.draw(player.region, player.getX(), player.getY(), 1, 1);

        for (Character npc : npcs) {
            spriteBatch.draw(npc.region, npc.getX(), npc.getY(), 1, 1);
        }
        spriteBatch.end();
    }

    private Portal portalAt(float x, float y)
    {
        for (Portal portal : portals) {
            if (portal.hit(x, y)) {
                return portal;
            }
        }
        return null;
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
        peopleTexture.dispose();
        spriteBatch.dispose();
    }
}
