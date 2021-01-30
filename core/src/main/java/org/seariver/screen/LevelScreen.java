package org.seariver.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import org.seariver.BaseActor;
import org.seariver.BaseScreen;
import org.seariver.TilemapActor;
import org.seariver.actor.Hero;
import org.seariver.actor.Solid;

public class LevelScreen extends BaseScreen {

    Hero hero;

    public void initialize() {

        TilemapActor tma = new TilemapActor("map.tmx", mainStage);

        for (MapObject obj : tma.getRectangleList("Solid")) {
            MapProperties props = obj.getProperties();
            new Solid((float) props.get("x"), (float) props.get("y"),
                    (float) props.get("width"), (float) props.get("height"),
                    mainStage);
        }

        MapObject startPoint = tma.getRectangleList("start").get(0);
        MapProperties startProps = startPoint.getProperties();
        hero = new Hero((float) startProps.get("x"), (float) startProps.get("y"), mainStage);
    }

    public void update(float deltaTime) {

        for (BaseActor solid : BaseActor.getList(mainStage, "org.seariver.actor.Solid")) {
            hero.preventOverlap(solid);
        }
    }
}
