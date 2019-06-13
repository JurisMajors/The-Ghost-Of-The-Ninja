package group4.UI;

import group4.AI.Evolver;
import group4.game.Main;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long window;
    private static int width = 1280;
    private static int height = 720;

    public Window() {
        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the actual window
        window = glfwCreateWindow(width, height, "Platformer", NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Window is NULL");
        }

        // Positioning the window in the center
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        // Setting the context and showing
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable v-sync
        if (!(!Evolver.render && Main.AI)) { // show the window if not training the AI or if Evolver.render is on
            // Create and set the custom cursor
            Cursor cursor = new Cursor("src/group4/res/textures/cursor32.png");
            glfwSetCursor(window, cursor.getCursorId());

            // Fiiiinally, show the window!
            glfwShowWindow(window);
        }
    }

    public long getWindowId() {
        return this.window;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int[] getSize() {
        return new int[]{width, height};
    }

    public static void setSize(int w, int h) {
        width = w;
        height = h;
    }

    /**
     * Updates the title of the window with the given string.
     *
     * @param title String, the new title of the window.
     */
    public void setWindowTitle(String title) {
        glfwSetWindowTitle(this.window, title);
    }

}
