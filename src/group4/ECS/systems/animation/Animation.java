package group4.ECS.systems.animation;

import com.badlogic.ashley.core.Entity;

public abstract class Animation {
    float currentT; // Implicitly always between [0,1]
    Entity target;
    float offsetT;


    public Animation(Entity target, float offsetT) {
        this.currentT = offsetT;
        this.offsetT = offsetT;
        this.target = target;
    }


    public boolean update(float deltaTime) {
        this.currentT = (this.currentT + deltaTime) % 1.0f;
        this.stepAnimation(deltaTime);
        return true;
    }


    protected abstract void stepAnimation(float deltaTime);
}
