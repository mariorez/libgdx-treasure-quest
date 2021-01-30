package org.seariver.screen;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import org.seariver.BaseScreen;
import org.seariver.TilemapActor;
import org.seariver.actor.Solid;

public class LevelScreen extends BaseScreen {

    public void initialize() {

        TilemapActor tma = new TilemapActor("map.tmx", mainStage);

        for (MapObject obj : tma.getRectangleList("Solid")) {
            MapProperties props = obj.getProperties();
            new Solid((float) props.get("x"), (float) props.get("y"),
                    (float) props.get("width"), (float) props.get("height"),
                    mainStage);
        }

    }

    public void update(float deltaTime) {
    }
}
