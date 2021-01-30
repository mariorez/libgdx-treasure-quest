package org.seariver;

import org.seariver.screen.LevelScreen;

public class TreasureQuestGame extends BaseGame {

    public void create() {
        super.create();
        setActiveScreen(new LevelScreen());
    }
}
