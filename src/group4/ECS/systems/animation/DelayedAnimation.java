package group4.ECS.systems.animation;

import com.badlogic.ashley.core.Entity;

public class DelayedAnimation extends Animation {
    private float delay;
    public DelayedAnimation(Entity target, float offsetT, float delay) {
        super(target, offsetT);
        this.delay = delay;
    }

    @Override
    protected void stepAnimation() {

    }

    public float getDelay() {
        return this.delay;
    }
}
