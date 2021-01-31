package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

public class Arrow extends BaseActor {

    public Arrow(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture("arrow.png");
        setSpeed(400);
    }

    public void act(float deltaTime) {
        super.act(deltaTime);
        applyPhysics(deltaTime);
    }
}

