package group4.ECS.etc;

import com.badlogic.ashley.core.ComponentMapper;
import group4.ECS.components.*;

/**
 * Mappers retrieve components from entities in O(1) time
 * use e.g. positionMapper.get(entity) to retrieve position data from entity
 */
public class Mappers {

    public static final ComponentMapper<PositionComponent> positionMapper =
            ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<PhysicsComponent> physicsMapper =
            ComponentMapper.getFor(PhysicsComponent.class);

    public static final ComponentMapper<GraphicsComponent> graphicsMapper =
            ComponentMapper.getFor(GraphicsComponent.class);

    public static final ComponentMapper<MovementComponent> movementMapper =
            ComponentMapper.getFor(MovementComponent.class);

    public static final ComponentMapper<ConsumableComponent> consumableMapper =
            ComponentMapper.getFor(ConsumableComponent.class);

    public static final ComponentMapper<StatsComponent> statsMapper =
            ComponentMapper.getFor(StatsComponent.class);

    public static final ComponentMapper<AudioComponent> audioMapper =
            ComponentMapper.getFor(AudioComponent.class);

}
