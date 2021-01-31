package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

public class Treasure extends BaseActor {

    public Treasure(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture("treasure-chest.png");
    }
}
