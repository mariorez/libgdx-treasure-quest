package org.seariver;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Iterator;

public class TilemapActor extends Actor {

    // window dimensions
    public static int windowWidth = 800;
    public static int windowHeight = 600;
    private TiledMap tiledMap;
    private OrthographicCamera tiledCamera;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;

    public TilemapActor(String filename, Stage theStage) {

        // set up tile map, renderer, and camera
        tiledMap = new TmxMapLoader().load(filename);
        int tileWidth = (int) tiledMap.getProperties().get("tilewidth");
        int tileHeight = (int) tiledMap.getProperties().get("tileheight");
        int numTilesHorizontal = (int) tiledMap.getProperties().get("width");
        int numTilesVertical = (int) tiledMap.getProperties().get("height");
        int mapWidth = tileWidth * numTilesHorizontal;
        int mapHeight = tileHeight * numTilesVertical;
        BaseActor.setWorldBounds(mapWidth, mapHeight);
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
        tiledMapRenderer.setBlending(true);
        tiledCamera = new OrthographicCamera();
        tiledCamera.setToOrtho(false, windowWidth, windowHeight);
        tiledCamera.update();
        theStage.addActor(this);
    }

    public void act(float deltaTime) {
        super.act(deltaTime);
    }

    public void draw(Batch batch, float parentAlpha) {
        // adjust tilemap camera to stay in sync with main camera
        Camera mainCamera = getStage().getCamera();
        tiledCamera.position.x = mainCamera.position.x;
        tiledCamera.position.y = mainCamera.position.y;
        tiledCamera.update();
        tiledMapRenderer.setView(tiledCamera);
        // need the following code to force batch order, otherwise it is batched and rendered last
        batch.end();
        tiledMapRenderer.render();
        batch.begin();
    }

    public ArrayList<MapObject> getRectangleList(String propertyName) {

        ArrayList<MapObject> list = new ArrayList<>();

        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {

                if (!(obj instanceof RectangleMapObject)) {
                    continue;
                }

                MapProperties props = obj.getProperties();
                if (props.containsKey("name") && props.get("name").equals(propertyName)) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    public ArrayList<MapObject> getTileList(String propertyName) {

        ArrayList<MapObject> list = new ArrayList<>();

        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof TiledMapTileMapObject)) {
                    continue;
                }

                MapProperties props = obj.getProperties();
                // Default MapProperties are stored within associated Tile object
                // Instance-specific overrides are stored in MapObject
                TiledMapTileMapObject tmtmo = (TiledMapTileMapObject) obj;
                TiledMapTile t = tmtmo.getTile();
                MapProperties defaultProps = t.getProperties();

                if (defaultProps.containsKey("name") && defaultProps.get("name").equals(propertyName)) {
                    list.add(obj);
                }

                // get list of default property keys
                Iterator<String> propertyKeys = defaultProps.getKeys();

                // iterate over keys; copy default values into props if needed
                while (propertyKeys.hasNext()) {
                    String key = propertyKeys.next();
                    // check if value already exists; if not, create property with default value
                    if (props.containsKey(key)) {
                        continue;
                    } else {
                        Object value = defaultProps.get(key);
                        props.put(key, value);
                    }
                }
            }
        }

        return list;
    }
}
