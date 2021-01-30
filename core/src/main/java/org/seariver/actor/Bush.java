package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Bush extends Solid {

    public Bush(float x, float y, Stage stage) {
        super(x, y, 32, 32, stage);
        loadTexture("bush.png");
        setBoundaryPolygon(8);
    }
}

