package group4.input;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {

    // true means this key is currently down
    public static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        // only turn on if the button is not released
        keys[key] = action != GLFW_RELEASE;
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }
}
