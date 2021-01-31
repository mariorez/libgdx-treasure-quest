package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

public class ShopArrow extends BaseActor {

    public ShopArrow(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture("arrow-icon.png");
    }
}
