package group4.ECS.etc;

import com.badlogic.ashley.core.Family;
import group4.ECS.components.*;

public class Families {

    // all entities of the graphicsFamily will get rendered, so they must possess graphics and a position
    public static final Family graphicsFamily = Family
            .all(GraphicsComponent.class, PositionComponent.class).get();

    // for all entities of the physicsFamily collision, gravity etc will be calculated
    public static final Family physicsFamily = Family
            .all(PhysicsComponent.class, PositionComponent.class, MovementComponent.class)
            .one(DimensionComponent.class, GraphicsComponent.class).get();

    // movement will be computed for each of the entities in movementFamily
    public static final Family movementFamily = Family
            .all(MovementComponent.class)
            .one(GraphicsComponent.class, DimensionComponent.class).get();

}
