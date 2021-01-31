package org.seariver.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.seariver.BaseActor;
import org.seariver.BaseGame;
import org.seariver.BaseScreen;
import org.seariver.TilemapActor;
import org.seariver.actor.Arrow;
import org.seariver.actor.Bush;
import org.seariver.actor.Coin;
import org.seariver.actor.DialogBox;
import org.seariver.actor.Flyer;
import org.seariver.actor.Hero;
import org.seariver.actor.NPC;
import org.seariver.actor.Rock;
import org.seariver.actor.Smoke;
import org.seariver.actor.Solid;
import org.seariver.actor.Sword;
import org.seariver.actor.Treasure;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys;

public class LevelScreen extends BaseScreen {

    Hero hero;
    Sword sword;
    Treasure treasure;

    int health;
    int coins;
    int arrows;
    boolean gameOver;

    Label healthLabel;
    Label coinLabel;
    Label arrowLabel;
    Label messageLabel;
    DialogBox dialogBox;

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

        for (MapObject obj : tma.getTileList("Coin")) {
            MapProperties props = obj.getProperties();
            new Coin((float) props.get("x"), (float) props.get("y"), mainStage);
        }

        for (MapObject obj : tma.getTileList("Flyer")) {
            MapProperties props = obj.getProperties();
            new Flyer((float) props.get("x"), (float) props.get("y"), mainStage);
        }

        MapObject treasureTile = tma.getTileList("Treasure").get(0);
        MapProperties treasureProps = treasureTile.getProperties();
        treasure = new Treasure((float) treasureProps.get("x"), (float) treasureProps.get("y"), mainStage);

        for (MapObject obj : tma.getTileList("NPC")) {
            MapProperties props = obj.getProperties();
            NPC npc = new NPC((float) props.get("x"), (float) props.get("y"), mainStage);
            npc.setId((String) props.get("id"));
            npc.setText((String) props.get("text"));
        }

        MapObject startPoint = tma.getRectangleList("start").get(0);
        MapProperties startProps = startPoint.getProperties();
        hero = new Hero((float) startProps.get("x"), (float) startProps.get("y"), mainStage);

        sword = new Sword(0, 0, mainStage);
        sword.setVisible(false);

        // User Interface
        health = 3;
        coins = 5;
        arrows = 3;
        gameOver = false;

        healthLabel = new Label(" x " + health, BaseGame.labelStyle);
        healthLabel.setColor(Color.PINK);

        coinLabel = new Label(" x " + coins, BaseGame.labelStyle);
        coinLabel.setColor(Color.GOLD);

        arrowLabel = new Label(" x " + arrows, BaseGame.labelStyle);
        arrowLabel.setColor(Color.TAN);

        messageLabel = new Label("...", BaseGame.labelStyle);
        messageLabel.setVisible(false);

        dialogBox = new DialogBox(0, 0, uiStage);
        dialogBox.setBackgroundColor(Color.TAN);
        dialogBox.setFontColor(Color.BROWN);
        dialogBox.setDialogSize(600, 100);
        dialogBox.setFontScale(0.80f);
        dialogBox.alignCenter();
        dialogBox.setVisible(false);

        BaseActor healthIcon = new BaseActor(0, 0, uiStage);
        healthIcon.loadTexture("heart-icon.png");
        BaseActor coinIcon = new BaseActor(0, 0, uiStage);
        coinIcon.loadTexture("coin-icon.png");
        BaseActor arrowIcon = new BaseActor(0, 0, uiStage);
        arrowIcon.loadTexture("arrow-icon.png");

        uiTable.pad(20);
        uiTable.add(healthIcon);
        uiTable.add(healthLabel);
        uiTable.add().expandX();
        uiTable.add(coinIcon);
        uiTable.add(coinLabel);
        uiTable.add().expandX();
        uiTable.add(arrowIcon);
        uiTable.add(arrowLabel);
        uiTable.row();
        uiTable.add(messageLabel).colspan(8).expandX().expandY();
        uiTable.row();
        uiTable.add(dialogBox).colspan(8);
    }

    // GAME LOOP
    public void update(float deltaTime) {

        if (gameOver) return;

        // hero ans sword movement controls
        if (!sword.isVisible()) {
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

            for (BaseActor flyer : BaseActor.getList(mainStage, "org.seariver.actor.Flyer")) {
                if (flyer.overlaps(solid)) {
                    flyer.preventOverlap(solid);
                    flyer.setMotionAngle(flyer.getMotionAngle() + 180);
                }
            }
        }

        for (BaseActor flyer : BaseActor.getList(mainStage, "org.seariver.actor.Flyer")) {
            if (hero.overlaps(flyer)) {
                hero.preventOverlap(flyer);
                flyer.setMotionAngle(flyer.getMotionAngle() + 180);
                Vector2 heroPosition = new Vector2(hero.getX(), hero.getY());
                Vector2 flyerPosition = new Vector2(flyer.getX(), flyer.getY());
                Vector2 hitVector = heroPosition.sub(flyerPosition);
                hero.setMotionAngle(hitVector.angleDeg());
                hero.setSpeed(100);
                health--;
            }

            if (sword.overlaps(flyer)) {
                flyer.remove();
                Coin coin = new Coin(0, 0, mainStage);
                coin.centerAtActor(flyer);
                Smoke smoke = new Smoke(0, 0, mainStage);
                smoke.centerAtActor(flyer);
            }
        }

        for (BaseActor arrow : BaseActor.getList(mainStage, "org.seariver.actor.Arrow")) {
            for (BaseActor flyer : BaseActor.getList(mainStage, "org.seariver.actor.Flyer")) {
                if (arrow.overlaps(flyer)) {
                    flyer.remove();
                    arrow.remove();
                    Coin coin = new Coin(0, 0, mainStage);
                    coin.centerAtActor(flyer);
                    Smoke smoke = new Smoke(0, 0, mainStage);
                    smoke.centerAtActor(flyer);
                }
            }

            for (BaseActor solid : BaseActor.getList(mainStage, "org.seariver.actor.Solid")) {
                if (arrow.overlaps(solid)) {
                    arrow.preventOverlap(solid);
                    arrow.setSpeed(0);
                    arrow.addAction(Actions.fadeOut(0.5f));
                    arrow.addAction(Actions.after(Actions.removeActor()));
                }
            }
        }

        for (BaseActor coin : BaseActor.getList(mainStage, "org.seariver.actor.Coin")) {
            if (hero.overlaps(coin)) {
                coin.remove();
                coins++;
            }
        }

        for (BaseActor npcActor : BaseActor.getList(mainStage, "org.seariver.actor.NPC")) {
            NPC npc = (NPC) npcActor;
            hero.preventOverlap(npc);
            boolean nearby = hero.isWithinDistance(4, npc);

            if (nearby && !npc.isViewing()) {
                // check NPC ID for dynamic text
                if (npc.getId().equals("Gatekeeper")) {
                    int flyerCount = BaseActor.count(mainStage, "org.seariver.actor.Flyer");
                    String message = "Destroy the Flyers and you can have the treasure. ";

                    if (flyerCount > 1) {
                        message += "There are " + flyerCount + " left.";
                    } else if (flyerCount == 1) {
                        message += "There is " + flyerCount + " left.";
                    } else { // flyerCount == 0
                        message += "It is yours!";
                        npc.addAction(Actions.fadeOut(5.0f));
                        npc.addAction(Actions.after(Actions.moveBy(-10000, -10000)));
                    }

                    dialogBox.setText(message);
                } else {
                    dialogBox.setText(npc.getText());
                }

                dialogBox.setVisible(true);
                npc.setViewing(true);
            }

            if (npc.isViewing() && !nearby) {
                dialogBox.setText(" ");
                dialogBox.setVisible(false);
                npc.setViewing(false);
            }
        }


        if (hero.overlaps(treasure)) {
            messageLabel.setText("You win!");
            messageLabel.setColor(Color.LIME);
            messageLabel.setFontScale(2);
            messageLabel.setVisible(true);
            treasure.remove();
            gameOver = true;
        }

        if (health <= 0) {
            messageLabel.setText("Game over...");
            messageLabel.setColor(Color.RED);
            messageLabel.setFontScale(2);
            messageLabel.setVisible(true);
            hero.remove();
            gameOver = true;
        }

        healthLabel.setText(" x " + health);
        coinLabel.setText(" x " + coins);
        arrowLabel.setText(" x " + arrows);
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

    public void shootArrow() {
        if (arrows <= 0) return;
        arrows--;
        Arrow arrow = new Arrow(0, 0, mainStage);
        arrow.centerAtActor(hero);
        arrow.setRotation(hero.getMotionAngle());
        arrow.setMotionAngle(hero.getMotionAngle());
    }

    public boolean keyDown(int keycode) {

        if (gameOver) return false;

        if (keycode == Keys.S) {
            swingSword();
        }

        if (keycode == Keys.A) {
            shootArrow();
        }

        return false;
    }
}
