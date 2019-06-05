package group4.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyBoard extends GLFWKeyCallback {

    // True means the key at this position is currently down
    public static boolean[] keys = new boolean[65536];

    /**
     * Gets called when input is received from the keyboard.
     */
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        // fn f11 xd
        if (key == -1) {
            return;
        }
        // only turn on if the button is not released
        keys[key] = action != GLFW_RELEASE;
    }

    /**
     * Returns true if key is down.
     */
    public static boolean isKeyDown(int key) {
        return keys[key];
    }
}
