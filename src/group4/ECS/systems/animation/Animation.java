package group4.ECS.systems.animation;

import javax.swing.text.html.parser.Entity;

public abstract class Animation {
    float currentT;
    Entity target;
    float offsetT;
    public Animation() {
        this.currentT = 0.0f;
    }

    public abstract void update(float deltaTime);
}
