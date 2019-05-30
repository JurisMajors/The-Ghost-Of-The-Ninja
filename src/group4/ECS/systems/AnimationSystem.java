package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.etc.Families;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() { super(Families.animationFamily); }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
