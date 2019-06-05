package group4.ECS.systems.animation;

public class DelayedAnimationSet extends AnimationSet {
    private float delay;
    /**
     * Construct an empty animation set
     */
    public DelayedAnimationSet(float delay) {
        super();
        this.delay = delay;
    }

    public float getDelay() {
        return this.delay;
    }
}
