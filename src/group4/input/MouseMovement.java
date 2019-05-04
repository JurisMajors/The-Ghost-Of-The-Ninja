package group4.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseMovement extends GLFWCursorPosCallback {

    // mouse coordinates on the screen
    public static double mouseX, mouseY;

    /**
     * Callback function that gets the mouse position on mouse movement.
     */
    @Override
    public void invoke(long window, double xpos, double ypos) {
        mouseX = xpos;
        mouseY = ypos;
    }
}
