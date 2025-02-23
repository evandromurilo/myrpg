package com.evandromurilo.myrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.evandromurilo.myrpg.*;
import com.evandromurilo.myrpg.Character;

public class MainGameScreen implements Screen {
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Character player;
    private Texture peopleTexture;
    private Texture creatureTexture;
    private SpriteBatch spriteBatch;
    private Level level;
    private MessageBox messageBox;
    private boolean showingInventory;
    private InventoryUI inventoryUI;
    private OrthographicCamera uiCamera;
    private FitViewport uiViewport;

    @Override
    public void show() {
        showingInventory = false;

        player = new Character(CharacterType.PLAYER);
        camera = new OrthographicCamera();

        // aqui eu digo que quero 30 x 20, que vai ser convertido para a escala
        // na prática, ele vai esticar os tiles até caberem 30 na horizontal e 20 na vertical
        camera.setToOrtho(false, 30, 20);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        spriteBatch = new SpriteBatch();

        player.teleport(3f, 30f);
        player.setItemBag(new ItemBag(3, 6, "Small bag"));
        Item sword = new Item("Sword", ItemType.WEAPON_GEAR);
        sword.addModifier(StatusType.ATTACK, 2);
        player.receiveItem(sword);

        GearSet playerGear = new GearSet();
        playerGear.addSlot(new GearSlot("Weapon", ItemType.WEAPON_GEAR));
        playerGear.addSlot(new GearSlot("Body", ItemType.BODY_GEAR));
        player.setGearSet(playerGear);

        peopleTexture = new Texture(Gdx.files.internal("People.png"));
        creatureTexture = new Texture(Gdx.files.internal("Creatures.png"));

        // aqui eu tenho o width e height real do sprite na spritesheet
        player.region = new TextureRegion(peopleTexture, 0, 0, 10, 10);

        messageBox = new MessageBox();
        loadMap("village.tmx");
    }

    public void loadMap(String mapName) {
        level = new Level(mapName, peopleTexture, creatureTexture, messageBox);
        level.addCharacter(player);
        if (renderer != null) {
            renderer.setMap(level.getMap());
        } else {
            // aqui são 1/10 porque o tileset é 10px por 10px
            renderer = new OrthogonalTiledMapRenderer(level.getMap(), 1 / 10f);
        }
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!showingInventory) {
            Gdx.input.setInputProcessor(messageBox.getStage());
            level.update(v);

            if (player.getState() == CharacterState.ON_PORTAL) {
                Portal portal = level.portalAt(player.getX(), player.getY());
                Gdx.app.debug("Map", String.format("Portal hit for %s", portal.getTargetMap()));
                loadMap(portal.getTargetMap());
                float targetY = portal.getTargetY();
                // flip y, porque salvamos as coordenadas do jeito que o tiled apresenta
                // nos outros lugares a framework faz o flip sozinha, com essa mesma conta inclusive
                targetY = (int) level.getHeight()- targetY - 1;
                player.teleport(portal.getTargetX(), targetY);
            }

            if (level.isPlayerTurn()) {
                if (Gdx.input.isKeyPressed(Input.Keys.J)) {
                    player.setTarget(0, -1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                    player.setTarget(0, 1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                    player.setTarget(-1, 0);
                } else if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                    player.setTarget(1, 0);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
                    player.endTurn();
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                    showingInventory = true;
                    inventoryUI = new InventoryUI(player.getItemBag(), player.getGearSet());
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                messageBox.clear();
            }
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                showingInventory = false;
            } else {
                inventoryUI.update(v);
            }
        }

        camera.position.x = player.getX();
        camera.position.y = player.getY();

        camera.update();
        renderer.setView(camera);
        renderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Character character : level.getCharacters()) {
            // aqui eu tenho o width e height na escala, então 1, 1 = 10x10
            spriteBatch.draw(character.region, character.getX(), character.getY(), 1, 1);
        }
        spriteBatch.end();

        messageBox.draw(v);

        if (showingInventory) {
            uiCamera.update();
            spriteBatch.setProjectionMatrix(uiCamera.combined);
            spriteBatch.begin();
            inventoryUI.render(v, spriteBatch);
            spriteBatch.end();
        }
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
        level.getMap().dispose();
        renderer.dispose();
        peopleTexture.dispose();
        spriteBatch.dispose();
    }
}
