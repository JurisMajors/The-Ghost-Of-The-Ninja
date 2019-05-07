package group4.engine;

import group4.AI.Ghost;
import group4.graphics.Camera;

public class GameState {
    private Player player;
    private Camera camera;
    private Level[] levels;
    private int score;

    GameState() {
        reset();
    }

    GameState(Ghost ghostPlayer) {

    }

    private void reset() {
        this.player = new Player();
    }
}
