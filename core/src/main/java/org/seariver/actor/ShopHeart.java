package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

public class ShopHeart extends BaseActor {

    public ShopHeart(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture("heart-icon.png");
    }
}
