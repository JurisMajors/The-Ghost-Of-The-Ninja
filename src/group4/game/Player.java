package group4.game;

import group4.maths.Vector3f;
import group4.input.KeyBoard;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Player {
    private float delta;
    public Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);

    public Player() {
        delta = 0.0f;
    }

    public void update() {
        position.x += delta;
        if (KeyBoard.isKeyDown(GLFW_KEY_RIGHT))
            delta = 0.15f;
        else
            delta += 0.0f;
    }
}
