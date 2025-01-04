package com.evandromurilo.myrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.ArrayList;

public class Level {
    private TiledMap map;
    private ArrayList<Portal> portals;
    private ArrayList<Character> characters;
    private String mapName;
    private Texture peopleTexture;

    public Level(String mapName, Texture peopleTexture) {
        map = new TmxMapLoader().load(mapName);
        this.mapName = mapName;
        portals = new ArrayList<>();
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
    }

    public TiledMap getMap() {
        return map;
    }

    public Portal portalAt(float x, float y) {
        for (Portal portal : portals) {
            if (portal.hit(x, y)) {
                return portal;
            }
        }
        return null;
    }

    public void update(float v) {
        for (Character character : characters) {
            character.update(v, map);
        }
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }
}

