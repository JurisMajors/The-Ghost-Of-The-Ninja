package group4.ECS.etc;

import com.badlogic.ashley.core.Family;
import group4.ECS.components.*;
import group4.ECS.components.events.EventComponent;
import group4.ECS.components.identities.*;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PhysicsComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.components.stats.RangeWeaponComponent;

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
    public static final Family animationFamily = Family
            .all(AnimationComponent.class).get();

    // all entities having some sort of audio
    public static final Family audioFamily = Family
            .all(AudioComponent.class).get();

    // all entities having a bounding box
    public static final Family gamestateFamily = Family
            .all(DimensionComponent.class).get();

    // all consumables, items
    public static final Family consumableFamily = Family
            .all(ConsumableComponent.class, PositionComponent.class).get();

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

    public static final Family bulletFamily = Family
            .all(BulletComponent.class).get();

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
            .all(RangeWeaponComponent.class).get();

    // player
    public static final Family playerFamily = Family
            .all(PlayerComponent.class).exclude(GhostComponent.class).get();

    public static final Family ghostFamily = Family
            .all(GhostComponent.class).get();

    //All entities with which collision is possible
    public static final Family collidableFamily = Family
            .all(CollisionComponent.class).exclude(MovementComponent.class, SplineComponent.class).get();

    // All moving collidable entities
    public static final Family collidableMovingFamily = Family
            .all(CollisionComponent.class, MovementComponent.class).get();

    // All entities with spline collision
    public static final Family collidableSplineFamily = Family
            .all(SplineComponent.class, CollisionComponent.class).exclude(SplinePathComponent.class).get();

    public static final Family allCollidableFamily = Family
            .all(CollisionComponent.class).get();

    // nonstatic damage inflicting entities (mobs, player, ghost..), static entities (traps..) excluded
    // static objects don't have variable bb's
    public static final Family dmgInflictingFamily = Family
            .all(DamageComponent.class, CollisionComponent.class, DimensionComponent.class, PositionComponent.class)
            .get();

    // all objects who can receive damage, i.e. have health
    public static final Family dmgReceivingFamily = Family
            .all(HealthComponent.class, CollisionComponent.class, DimensionComponent.class,
                    PositionComponent.class)
            .get();

    // all entities which react to a event event
    public static final Family timedEventFamily = Family.all(EventComponent.class).get();

    public static final Family graphFamily = Family
            .all(GraphComponent.class).get();

    public static final Family aStarMobFamily = Family
            .all(AStarMobComponent.class).get();

    public static final Family pathFamily = Family
            .all(PathComponent.class).get();
}
