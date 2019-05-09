package group4.engine;

import group4.AI.Ghost;
import group4.game.Player;
import group4.graphics.Camera;
import group4.graphics.VertexArray;
import group4.maths.Matrix4f;

public class GameState {
    private Player player;
    private Camera activeCamera;
    private VertexArray level;
    private int score;
    Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);

    public GameState() {
        reset();
    }

    // For initializing with an AI player
    public GameState(Ghost ghostPlayer) {
        // TODO: implement
    }

    private void reset() {
        this.player = new Player();
        this.activeCamera = new Camera();
    }

    public Player getPlayer() {
        return this.player;
    }

    public VertexArray getDynamicEntities() {
        // TODO: Returns VertexArray as the code is incomplete
        return this.level;
    }

    public Camera getActiveCamera() {
        return this.activeCamera;
    }
}
