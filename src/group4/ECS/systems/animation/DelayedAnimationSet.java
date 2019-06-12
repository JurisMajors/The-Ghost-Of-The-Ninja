package group4.ECS.systems.animation;

public class DelayedAnimationSet extends AnimationSet {
    private int frames;
    private int currentFrame;

    /**
     * Construct an empty animation set
     */
    public DelayedAnimationSet(int frames) {
        super();
        this.frames = frames;
        this.currentFrame = 1;
    }

    /**
     * Update all animations in this set
     */
    @Override
    public boolean update(float deltaTime) {
        System.out.println(this.currentFrame / (float) this.frames);
        for (Animation a : this.animations) {
            a.update(this.currentFrame / (float) this.frames);
        }

        // Move to next timestep for this animation
        this.currentFrame++;

        // Report wether the delayed animation has played all of its frames
        if (this.currentFrame >= this.frames) {
            this.currentFrame = 1;
            return true;
        } else {
            return false;
        }
    }
}
