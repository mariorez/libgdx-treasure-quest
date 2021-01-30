package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Rock extends Solid {

    public Rock(float x, float y, Stage stage) {
        super(x, y, 32, 32, stage);
        loadTexture("rock.png");
        setBoundaryPolygon(8);
    }
}

