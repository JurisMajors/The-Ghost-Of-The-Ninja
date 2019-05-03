public class Timer {
    private int FPS;
    private double frameTime;

    private double timeUnprocessed;
    private double timePassed;

    private int counter;

    private double currentTime;
    private double previousTime;

    public Timer() {
        FPS = 60;
        frameTime = 1.0 / (double) FPS;
        previousTime = 0.0;
        timePassed = 0.0;
        counter = 0;
        timeUnprocessed = 0;
    }

    public double getDeltaTime() {
        currentTime = (double) System.nanoTime() / (double) 1000000000L;
        double delta = previousTime - currentTime;
        previousTime = currentTime;
        timePassed += delta;
        return delta;
    }

    public void nextFrame() {
        counter++;
    }
}
