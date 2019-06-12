package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;

public class MobComponent implements Component {

    public float currentVisionRange = 4.0f;

    public AbstractMovementHandler handler;

    public MobComponent(AbstractMovementHandler handler) {
        this.handler = handler;
    }

    public MobComponent() {

    }
}
