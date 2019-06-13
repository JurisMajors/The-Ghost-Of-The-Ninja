package group4.game;

import com.badlogic.ashley.core.Engine;
import group4.AI.Evolver;
import group4.ECS.entities.Camera;
import group4.ECS.etc.Families;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.AStarPathSystem;
import group4.ECS.systems.CameraSystem;
import group4.ECS.systems.PathMovementSystem;
import group4.ECS.systems.RenderSystem;
import group4.ECS.systems.animation.AnimationSystem;
import group4.ECS.systems.collision.CollisionEventSystem;
import group4.ECS.systems.collision.CollisionSystem;
import group4.ECS.systems.collision.LastSystem;
import group4.ECS.systems.collision.UncollidingSystem;
import group4.ECS.systems.combat.PlayerCombatSystem;
import group4.ECS.systems.death.GhostDyingSystem;
import group4.ECS.systems.death.MobDyingSystem;
import group4.ECS.systems.death.PlayerDyingSystem;
import group4.ECS.systems.event.EventSystem;
import group4.ECS.systems.movement.BulletMovementSystem;
import group4.ECS.systems.movement.GhostMovementSystem;
import group4.ECS.systems.movement.MobMovementSystem;
import group4.ECS.systems.movement.PlayerMovementSystem;
import group4.UI.StartScreen;
import group4.UI.Window;
import group4.audio.Sound;
import group4.graphics.ImageSequence;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.TileMapping;
import group4.input.KeyBoard;
import group4.input.MouseClicks;
import group4.input.MouseMovement;
import group4.levelSystem.Level;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.opengl.GL11.*;

public class Main implements Runnable {
    private Thread thread;
    /**
     * enable this if you want to run the genetic algorithm, instead of playing urself
     **/
    public static final boolean AI = false;
    /**
     * whether should do calls to OPENGL
     **/
    public static final boolean SHOULD_OPENGL = !Main.AI || Evolver.render;

    private Window win;
    public static long window; // The id of the window

    private Audio audio;

    private Level level;
    private Engine engine;
    private Camera camera;
    private StartScreen startScreen;

    public static final float SCREEN_WIDTH = 32.0f;
    public static final float SCREEN_HEIGHT = SCREEN_WIDTH * 9.0f / 16.0f;
    public static GameState STATE;

    /**
     * Creates a new thread on which it wel run() the game.
     */
    public void start() {
        this.thread = new Thread(this, "Game");
        this.thread.start(); // This implicitly calls run(), given that this class implements Runnable
    }

    /**
     * The main logic for controlling the program flow of this class.
     */
    public void run() {
        if (Main.SHOULD_OPENGL) {
            init();
        } else {
            TileMapping.loadAllTileMappings();
        }
        if (AI) {
            Evolver.train();
        } else {
            loop();
        }
        if (Main.SHOULD_OPENGL) {
            // Cleanup after we exit the game loop
            glfwFreeCallbacks(window); // Free the window callbacks
            glfwDestroyWindow(window); // Destroy the window

            // Terminate GLFW and free the error callback
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
        //Terminate OpenAL
        // delete one audio thing
//        alDeleteSources(sourcePointer);
//        alDeleteBuffers(bufferPointer);

        // delete context
        alcDestroyContext(audio.audioContext);
        alcCloseDevice(audio.audio);

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
        this.win = new Window();
        this.window = win.getWindowId();

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(this.window, new KeyBoard());
        // Setup mouse button callback. It will be called when a button is pressed on the mouse.
        glfwSetMouseButtonCallback(this.window, new MouseClicks());
        // Setup mouse movement callback. It will be called when the mouse moves.
        glfwSetCursorPosCallback(this.window, new MouseMovement());

        // Some additional OpenGL configuration
        GL.createCapabilities(); // Enable OpenGL bindings for usage by GLFW. Critical!
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Load all required resources for the game
        Shader.loadAllShaders();
        Texture.loadAllTextures();
        TileMapping.loadAllTileMappings();
        ImageSequence.loadAllImageSequences();

        // Initialize the engine
        this.engine = TheEngine.getInstance();
        if (!AI) {
            // load audio
            audio = new Audio();
            Sound.loadAllSounds();
            // play background sound
            Sound.BACKGROUND.play();
            // Set up all engine systems
            // Systems which change the gamestate
            this.engine.addSystem(new EventSystem(0));
            this.engine.addSystem(new AStarPathSystem(2));
            this.engine.addSystem(new PathMovementSystem(3));

            this.engine.addSystem(new PlayerMovementSystem(1));
            this.engine.addSystem(new GhostMovementSystem(2));
            this.engine.addSystem(new MobMovementSystem(3));
            this.engine.addSystem(new BulletMovementSystem(4));
            this.engine.addSystem(new PlayerCombatSystem(5));
            this.engine.addSystem(new CollisionSystem(6));
            this.engine.addSystem(new CollisionEventSystem(7));
            this.engine.addSystem(new UncollidingSystem(8));
            this.engine.addSystem(new PlayerDyingSystem(true, 9));
            this.engine.addSystem(new GhostDyingSystem(false, 10));
            this.engine.addSystem(new MobDyingSystem(11));
            this.engine.addSystem(new AnimationSystem(12));

            // Systems which are essentially observers of the changed gamestate
            this.engine.addSystem(new CameraSystem(Families.playerFamily, 13));
            this.engine.addSystem(new RenderSystem(14));
            this.engine.addSystem(new LastSystem(15));

            // Initialize the StartScreen, this will load the level
            this.startScreen = new StartScreen();
        }

        // Set up a camera for our game
        this.camera = new Camera();
        this.engine.addEntity(camera); // Adding the camera to the module (which adds it to the engine?)
    }

    /**
     * The main game loop where we update the GameState and render (if required).
     */
    private void loop() {
        long lastLoopTime = System.nanoTime();
        final int targetFps = 60;
        final long optimalTime = (long) 1e9 / targetFps;
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

            // pass on delta in seconds
            double delta = updateLength / 1e9;

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= (long) 1e9) {
                this.win.setWindowTitle("(FPS: " + fps + ")");
                lastFpsTime = 0;
                fps = 0;
            }

            if (STATE == GameState.PLAYING || STATE == GameState.STARTSCREEN) {
                if (STATE == GameState.STARTSCREEN) {
                    this.startScreen.update(); // Allows for the startscreen logic to update.. Should perhaps be an entity? But this works.
                }
                this.engine.update((float) delta); // Update the gamestate
            }

            glfwSwapBuffers(this.window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // check if the user wants to exit the game
            if (KeyBoard.isKeyDown(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(this.window, true);
            }
            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try {
                Thread.sleep((lastLoopTime - System.nanoTime() + optimalTime) / (long) 1e6);
            } catch (Exception e) {
                continue;
            }
        }
    }

    public static void setState(GameState state) {
        STATE = state;
    }

    public static void main(String[] args) {
        if (Main.AI && args.length != 0) {
            Evolver.parseArgs(args);
        }
        new Main().start();
    }
}
