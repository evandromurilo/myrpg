package com.evandromurilo.myrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.*;
import java.util.stream.Collectors;

public class Level {
    private TiledMap map;
    private ArrayList<Portal> portals;
    private ArrayList<Character> characters;
    private String mapName;
    private Texture peopleTexture;
    private Texture creatureTexture;
    private MessageBox messageBox;
    private Queue<Character> turnQueue;

    public Level(String mapName, Texture peopleTexture, Texture creatureTexture, MessageBox messageBox) {
        map = new TmxMapLoader().load(mapName);
        this.mapName = mapName;
        this.messageBox = messageBox;
        this.creatureTexture = creatureTexture;
        portals = new ArrayList<>();
        turnQueue = new LinkedList<>();

        MapLayer portalLayer = map.getLayers().get("Portals");
        for (MapObject obj : portalLayer.getObjects()) {
            portals.add(new Portal(obj));
        }

        characters = new ArrayList<>();
        MapLayer npcLayer = map.getLayers().get("NPC");
        for (MapObject obj : npcLayer.getObjects()) {
            Character character = new Character(obj, peopleTexture);
            characters.add(character);
        }

        Character monster = new Character(CharacterType.MONSTER);
        monster.region = new TextureRegion(creatureTexture, 0, 0, 10, 10);
        monster.teleport(3f, 35f);
        characters.add(monster);

        refillTurnQueue();

        if (!isPlayerTurn()) {
            Character character = turnQueue.element();
            character.chooseAction(this);
        }
    }

    public boolean hasCollision(float x, float y) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Collision");
        return layer.getCell((int) x, (int) y) != null;
    }

    public boolean hasBaseTile(float x, float y) {
        TiledMapTileLayer base = (TiledMapTileLayer) map.getLayers().get("Base");
        return base.getCell((int) x, (int) y) != null;
    }

    public boolean hasCharacter(float x, float y) {
        return characterAt(x, y) != null;
    }

    public TiledMap getMap() {
        return map;
    }

    public int getHeight() {
        return (int) map.getProperties().get("height");
    }

    public Portal portalAt(float x, float y) {
        for (Portal portal : portals) {
            if (portal.hit(x, y)) {
                return portal;
            }
        }
        return null;
    }

    public Character characterAt(float x, float y) {
        for (Character character : characters) {
            if (character.getX() == x && character.getY() == y) {
                return character;
            }
        }
        return null;
    }

    public boolean isPlayerTurn() {
        Character character = turnQueue.element();
        return character.getType() == CharacterType.PLAYER && character.getState() == CharacterState.IDLE;
    }

    public void update(float v) {
        while (turnQueue.element().getState() == CharacterState.FINISHED_ACTION) {
            Character head = turnQueue.element();

            if (head.getState() == CharacterState.FINISHED_ACTION) {
                head.setState(CharacterState.IDLE);
                turnQueue.remove();

                if (turnQueue.isEmpty()) {
                    refillTurnQueue();
                }

                head = turnQueue.element();

                if (head.getType() != CharacterType.PLAYER) { // the player action is chosen on the game loop, by key input
                    head.chooseAction(this);
                }
            }
        }


        for (Character character : characters) {
            character.update(v, this);
        }

        characters.removeIf(c -> c.getState() == CharacterState.DEAD);
    }

    /**
     * Cada novo turno tem sua própria ordem, assim se houver uma alteração de velocidade, vai
     * ser refletida no turno seguinte.
     */
    private void refillTurnQueue() {
        turnQueue.clear();
        characters.sort(Comparator.comparing(Character::getSpeed));
        turnQueue.addAll(characters);
    }

    public void echo(String message) {
        messageBox.push(message);
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }
}

