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

    // all moving entities, i.e. having a velocity and a position
    public static final Family movementFamily = Family
            .all(MovementComponent.class, PositionComponent.class).get();

    // all entities which require animation
    // TODO: for now this is the same as graphics, how to represent animation?
    public static final Family animationFamily = Family
            .all(PositionComponent.class, GraphicsComponent.class).get();

    // all entities having some sort of audio
    public static final Family audioFamily = Family
            .all(AudioComponent.class).get();

    // all entities having a bounding box
    public static final Family gamestateFamily = Family
            .all(DimensionComponent.class).get();

    // all consumables, items
    public static final Family consumableFamily = Family
            .all(ConsumableComponent.class, PositionComponent.class).get();

    // all enemies, the player and destructible objects
    public static final Family combatFamily = Family
            .all(PositionComponent.class, DimensionComponent.class, StatsComponent.class, GraphicsComponent.class).get();

    public static final Family ghostFamily = Family
            .all(GhostComponent.class).get();

    // all cameras
    public static final Family cameraFamily = Family
            .all(CameraComponent.class).get();

    // all platforms
    public static final Family platformFamily = Family
            .all(PlatformComponent.class).get();

    // all moving platforms
    public static final Family movingPlatformFamily = Family
            .all(MovementComponent.class, PlatformComponent.class).get();

    // all moving entities except platforms
    public static final Family movingNonPlatformFamily = Family
            .all(MovementComponent.class).exclude(PlatformComponent.class).get();

    // all moving mobs except flying mobs
    public static final Family movingGravityMobFamily = Family
            .all(GravityComponent.class, MovementComponent.class, MobComponent.class).get();

    // all flying mobs
    public static final Family movingNonGravityMobFamily = Family
            .all(MovementComponent.class, MobComponent.class).exclude(GravityComponent.class).get();

    // player
    public static final Family playerFamily = Family
            .all(MovementComponent.class, PlayerComponent.class)
            .exclude(GhostComponent.class).get();

    //All entities with which collision is possible
    public static final Family collidableFamily = Family
            .all(PositionComponent.class, DimensionComponent.class).get();
    //All entities with which collision is possible
    public static final Family collidableMovingFamily = Family
            .all(PositionComponent.class, MovementComponent.class, DimensionComponent.class).get();
}
