package group4.ECS.etc;

import com.badlogic.ashley.core.Family;
import group4.ECS.components.AudioComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.ShootingComponent;
import group4.ECS.components.SplineComponent;
import group4.ECS.components.identities.*;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PhysicsComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;

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
            .all(PositionComponent.class, DimensionComponent.class, HealthComponent.class, GraphicsComponent.class).get();

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

    public static final Family mobFamily = Family
            .all(MobComponent.class).get();

    // all walking mobs
    public static final Family walkingMobFamily = Family
            .all(WalkingMobComponent.class).get();

    // all jumping&walking mobs
    public static final Family jumpingWalkingMobFamily = Family
            .all(JumpingWalkingMobComponent.class).get();

    // all jumping mobs
    public static final Family jumpingMobFamily = Family
            .all(JumpingMobComponent.class).get();

    // all flapping mobs
    public static final Family flappingMobFamily = Family
            .all(FlappingMobComponent.class).get();

    // all flying mobs
    public static final Family flyingMobFamily = Family
            .all(FlyingMobComponent.class).get();

    // all shooting entities
    public static final Family shootingFamily = Family
            .all(ShootingComponent.class).get();

    // all bullets
    public static final Family bulletFamily = Family
            .all(BulletComponent.class).get();

    // player
    public static final Family playerFamily = Family
            .all(PlayerComponent.class).exclude(GhostComponent.class).get();

    public static final Family ghostFamily = Family
            .all(GhostComponent.class).get();

    //All entities with which collision is possible
    public static final Family collidableFamily = Family
            .all(CollisionComponent.class).exclude(MovementComponent.class).get();
    //All entities with which collision is possible
    public static final Family collidableMovingFamily = Family
            .all(CollisionComponent.class, MovementComponent.class).get();

    //All entities with spline collision
    public static final Family collidableSplineFamily = Family
            .all(SplineComponent.class, CollisionComponent.class).get();
}
