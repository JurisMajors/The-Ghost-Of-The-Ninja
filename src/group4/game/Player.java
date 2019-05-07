package group4.game;

import group4.graphics.VertexArray;
import group4.maths.Vector3f;
import group4.input.KeyBoard;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Player {
    private float delta;
    public Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
    private VertexArray geo;

    public Player() {
        delta = 0.0f;
        // TODO: isDrawable, hence needs a shape.
//        geo = new VertexArray()
    }

    public void update() {
        position.x += delta;
        if (KeyBoard.isKeyDown(GLFW_KEY_RIGHT))
            delta = 0.15f;
        else
            delta += 0.0f;
    }

    public void render() {

    }
}
