package group4.game;

import com.badlogic.ashley.core.Engine;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.CameraSystem;
import group4.ECS.systems.MovementSystem;
import group4.ECS.systems.RenderSystem;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.input.KeyBoard;
import group4.input.MouseClicks;
import group4.input.MouseMovement;
import group4.levelSystem.Level;
import group4.levelSystem.levels.SimpleLevel;
import group4.maths.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;


import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main implements Runnable {
    private Thread thread;

    private Window win;
    private long window; // The id of the window

    private Timer timer;
    private Engine engine;
    private Level level;

    public static final float SCREEN_WIDTH = 20.0f;
    public static final float SCREEN_HEIGHT = SCREEN_WIDTH * 9.0f / 16.0f;


    /**
     * Creates a new thread on which it wel run() the game.
     */
    public void start() {
        thread = new Thread(this, "Game");
        thread.start(); // This implicitly calls run(), given that this class implements Runnable
    }

    /**
     * The main logic for controlling the program flow of this class.
     */
    public void run() {
        init();
        loop();

        // Cleanup after we exit the game loop
        glfwFreeCallbacks(window); // Free the window callbacks
        glfwDestroyWindow(window); // Destroy the window

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Function for all our initialization logic. E.g.:
     * - create the window
     * - create callbacks
     * - ...
     */
    private void init() {
        // Setup an error callback to output errors to System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Create the window
        win = new Window();
        window = win.getWindowId();

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, new KeyBoard());
        // Setup mouse button callback. It will be called when a button is pressed on the mouse.
        glfwSetMouseButtonCallback(window, new MouseClicks());
        // Setup mouse movement callback. It will be called when the mouse moves.
        glfwSetCursorPosCallback(window, new MouseMovement());

        // Some additional OpenGL configuration
        GL.createCapabilities(); // Enable OpenGL bindings for usage by GLFW. Critical!
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Preload all resources
        Shader.loadAllShaders();
        Texture.loadAllTextures();

        // Initialize the engine
        engine = TheEngine.getInstance();

        // Set up all engine systems (NOTE: order is important here as we do not yet use ordering within the engine I believe)
        engine.addSystem(new CameraSystem()); // CameraSystem must be added before RenderSystem
        engine.addSystem(new MovementSystem()); // TODO: Probably temp and should be changed when the new movement system is ready
        engine.addSystem(new RenderSystem());

        // Initialize the level
        this.level = new SimpleLevel();
    }

    /**
     * The main game loop where we update the GameState and render (if required).
     */
    private void loop() {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        double lastFpsTime = 0.0;
        int fps = 0;
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000)
            {
                System.out.println("(FPS: "+fps+")");
                lastFpsTime = 0;
                fps = 0;
            }

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            TheEngine.getInstance().update((float) delta); // Update the gamestate

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // check if the user wants to exit the game
            if (KeyBoard.isKeyDown(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(window, true);
            }
            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try {
                Thread.sleep((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            } catch (Exception e) {
                continue;
            }
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }

}
