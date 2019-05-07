package group4.ECS.etc;

import com.badlogic.ashley.core.Family;
import group4.ECS.components.*;

/**
 * This class determines groups (families) of entities which share the same components
 * such that systems can easily retrieve corresponding entities
 */
public class Families {

    // all entities of the graphicsFamily will get rendered, so they must possess graphics and a position
    public static final Family graphicsFamily = Family
            .all(GraphicsComponent.class, PositionComponent.class).get();

    // all entities of the graphicsFamily will get rendered, so they must possess graphics and a position
    public static final Family physicsFamily = Family
            .all(PhysicsComponent.class, PositionComponent.class, MovementComponent.class).get();

    // all moving entities, i.e. having a velocity and a position and an input (explicit movement)
    public static final Family movementFamily = Family
            .all(MovementComponent.class, PositionComponent.class, AIInputComponent.class).get();

    // all entities which require animation
    public static final Family animationFamily = Family
            .all(PositionComponent.class, GraphicsComponent.class).get();

    // all entities having some sort of audio
    public static final Family audioFamily = Family
            .all(AudioComponent.class).get();

    // all consumables, items
    public static final Family consumableFamily = Family
            .all(ConsumableComponent.class, PositionComponent.class).get();

    // all enemies, the player and destructible objects
    public static final Family combatFamily = Family
            .all(PositionComponent.class, StatsComponent.class, GraphicsComponent.class).get();



}
