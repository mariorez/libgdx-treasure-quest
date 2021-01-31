package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

public class NPC extends BaseActor {

    // ID used for specific graphics and identifying NPCs with dynamic messages
    private String id;
    // the text to be displayed
    private String text;
    // used to determine if dialog box text is currently being displayed
    private boolean viewing;

    public NPC(float x, float y, Stage stage) {
        super(x, y, stage);
        text = " ";
        viewing = false;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setViewing(boolean viewing) {
        this.viewing = viewing;
    }

    public boolean isViewing() {
        return viewing;
    }

    public void setId(String id) {
        this.id = id;
        if (this.id.equals("Gatekeeper")) {
            loadTexture("npc-1.png");
        } else if (this.id.equals("Shopkeeper")) {
            loadTexture("npc-2.png");
        } else { // default image
            loadTexture("npc-3.png");
        }
    }

    public String getId() {
        return id;
    }
}
