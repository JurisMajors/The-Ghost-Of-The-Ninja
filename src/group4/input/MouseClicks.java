package group4.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

public class MouseClicks extends GLFWMouseButtonCallback {

    private static boolean leftMouseButton = false;
    private static boolean rightMouseButton = false;

    /**
     * Callback function that gets click data on mouse clicks.
     */
    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_LEFT) {
            leftMouseButton = action == GLFW_PRESS;
        }
        if (button == GLFW_MOUSE_BUTTON_RIGHT) {
            rightMouseButton = action == GLFW_PRESS;
        }
    }

    /**
     * Returns true if the left mouse button is down.
     */
    public static boolean leftMouseDown() {
        return leftMouseButton;
    }

    /**
     * Returns true if the right mouse button is down.
     */
    public static boolean rightMouseDown() {
        return rightMouseButton;
    }
}
