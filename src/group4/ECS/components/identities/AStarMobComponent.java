package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;
import group4.ECS.systems.GraphHandlers.AbstractGraphHandler;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;

public class AStarMobComponent implements Component {

    public float currentVisionRange = 4.0f;

    public AbstractGraphHandler handler;

    public AStarMobComponent(AbstractGraphHandler handler) {
        this.handler = handler;
    }
}
