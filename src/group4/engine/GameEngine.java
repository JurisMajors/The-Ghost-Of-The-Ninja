package group4.engine;

/**
 * GameEngine / Engine class which stores the game's logic.
 */
public class GameEngine {
    // Private so nobody makes an instance
    private GameEngine() {

    }

    /**
     * Updates the given GameState 1 frame ahead.
     * @param gs GameState, contains all information regarding the current state space of the game
     */
    public static void update(GameState gs) {
        // Simplified, as I don't have all entities etc yet.
        gs.getPlayer().update();
        gs.getActiveCamera().update();
//        gs.getDynamicEntities().update();

        // Additional logic...
    }
}
