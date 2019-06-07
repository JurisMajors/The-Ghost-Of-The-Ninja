package group4.ECS.components.events;

import com.badlogic.ashley.core.Entity;

@FunctionalInterface
public interface Function {

    void invoke(Entity entity);

}
