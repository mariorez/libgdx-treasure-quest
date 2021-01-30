package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

public class Sword extends BaseActor {

    public Sword(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture("sword.png");
    }
}
