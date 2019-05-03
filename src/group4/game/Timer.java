package group4.game;

public class Timer {
    private int targetFPS; // The target targetFPS
    private double frameTime; // The target frame time, 1/targetFPS

    private double deltaTime; // The time we're running behind since last update

    private int frameCounter; // How many frames we render each second
    private double elapsedTime; // For tracking the second.

    private double currentTime;
    private double previousTime;

    /**
     * Initializations via the constructor
     */
    public Timer() {
        targetFPS = 60;
        frameTime = 1.0 / (double) targetFPS;

        deltaTime = 0.0;

        frameCounter = 0;
        elapsedTime = 0.0;

        previousTime = this.getTime(); // NOW is the starting time for the timer
    }

    /**
     * Gets the time difference between a current call and the previous call, and keeps track of
     * the total time we are running behind on running the target targetFPS. (So we don't skip frames)
     *
     * Additionally tracks the total time elapsed since the last measuring point for the targetFPS the
     * game actually achieves.
     *
     * @return double, the total time we are running behind true 60FPS.
     */
    public double getDeltaTime() {
        // Find delta since last update
        currentTime = this.getTime();
        double delta = currentTime - previousTime;
        previousTime = currentTime;

        deltaTime += delta;   // Add it to the time we're behind
        elapsedTime += delta; // Add it to the time since last measuring point
        return deltaTime;
    }

    /**
     * Calling nextFrame() indicates to the timer that the Game handled a frame.
     * I.e. the total time we were running behind was larger than the time for a single frame, hence
     * the Game will process another frame, and we're decreasing the time we're running behind.
     */
    public void nextFrame() {
        deltaTime -= frameTime; // Reduce the deltaTime with the amount of time a single frame should take
        frameCounter++;         // Keep track of the number of frames we rendered since last measurement

        // If the elapsedTime at this point is greater than 1.0 second, then the frameCounter is our new
        // measurement for the actual targetFPS we obtain.
        if (elapsedTime >= 1.0) {
            // TODO: As we do not have any visual debug utils yet, we print. Change this in the future
            //       to be a visual overlay on the game.
            System.out.println("Real FPS: " + frameCounter); // Log

            elapsedTime = 0;
            frameCounter = 0;
        }
    }

    /**
     * Gives back 1.0 / targetFPS.
     * @return double, the intended time delta between two frames.
     */
    public double getFrameTime() {
        return this.frameTime;
    }

    /**
     * Gives the current system time as a double.
     * @return double, the current system time.
     */
    private double getTime() {
        return (double) System.nanoTime() / (double) 1000000000L;
    }
}
