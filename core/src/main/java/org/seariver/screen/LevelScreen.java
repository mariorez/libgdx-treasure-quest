package org.seariver.screen;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.seariver.BaseActor;
import org.seariver.BaseScreen;
import org.seariver.TilemapActor;
import org.seariver.actor.Bush;
import org.seariver.actor.Hero;
import org.seariver.actor.Rock;
import org.seariver.actor.Solid;
import org.seariver.actor.Sword;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys;

public class LevelScreen extends BaseScreen {

    Hero hero;
    Sword sword;

    public void initialize() {

        TilemapActor tma = new TilemapActor("map.tmx", mainStage);

        for (MapObject obj : tma.getRectangleList("Solid")) {
            MapProperties props = obj.getProperties();
            new Solid((float) props.get("x"), (float) props.get("y"),
                    (float) props.get("width"), (float) props.get("height"),
                    mainStage);
        }

        for (MapObject obj : tma.getTileList("Bush")) {
            MapProperties props = obj.getProperties();
            new Bush((float) props.get("x"), (float) props.get("y"), mainStage);
        }

        for (MapObject obj : tma.getTileList("Rock")) {
            MapProperties props = obj.getProperties();
            new Rock((float) props.get("x"), (float) props.get("y"), mainStage);
        }

        MapObject startPoint = tma.getRectangleList("start").get(0);
        MapProperties startProps = startPoint.getProperties();
        hero = new Hero((float) startProps.get("x"), (float) startProps.get("y"), mainStage);

        sword = new Sword(0, 0, mainStage);
        sword.setVisible(false);
    }

    // GAME LOOP
    public void update(float deltaTime) {

        if (!sword.isVisible()) {
            // hero movement controls
            if (input.isKeyPressed(Keys.LEFT)) hero.accelerateAtAngle(180);
            if (input.isKeyPressed(Keys.RIGHT)) hero.accelerateAtAngle(0);
            if (input.isKeyPressed(Keys.UP)) hero.accelerateAtAngle(90);
            if (input.isKeyPressed(Keys.DOWN)) hero.accelerateAtAngle(270);
        } else {
            for (BaseActor bush : BaseActor.getList(mainStage, "org.seariver.actor.Bush")) {
                if (sword.overlaps(bush)) bush.remove();
            }
        }

        for (BaseActor solid : BaseActor.getList(mainStage, "org.seariver.actor.Solid")) {
            hero.preventOverlap(solid);
        }
    }

    public void swingSword() {

        // visibility determines if sword is currently swinging
        if (sword.isVisible()) return;

        hero.setSpeed(0);

        float facingAngle = hero.getFacingAngle();

        Vector2 offset = new Vector2();
        if (facingAngle == 0) {
            offset.set(0.50f, 0.20f);
        } else if (facingAngle == 90) {
            offset.set(0.65f, 0.50f);
        } else if (facingAngle == 180) {
            offset.set(0.40f, 0.20f);
        } else { // facingAngle == 270
            offset.set(0.25f, 0.20f);
        }

        sword.setPosition(hero.getX(), hero.getY());
        sword.moveBy(offset.x * hero.getWidth(), offset.y * hero.getHeight());
        float swordArc = 90;
        sword.setRotation(facingAngle - swordArc / 2);
        sword.setOriginX(0);
        sword.setVisible(true);
        sword.addAction(Actions.rotateBy(swordArc, 0.25f));
        sword.addAction(Actions.after(Actions.visible(false)));

        // hero should appear in front of sword when facing north or west
        if (facingAngle == 90 || facingAngle == 180) {
            hero.toFront();
        } else {
            sword.toFront();
        }
    }

    public boolean keyDown(int keycode) {
        if (keycode == Keys.S) {
            swingSword();
        }
        return false;
    }
}
