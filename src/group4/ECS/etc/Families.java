package group4.ECS.etc;

import com.badlogic.ashley.core.Family;
import group4.ECS.components.*;

/**
 * This class determines groups (families) of entities which share the same components
 * such that systems can easily retrieve corresponding entities
 */
public class Families {

    //All platforms
    public static final Family platformFamily = Family.all(PlatformComponent.class).get();

    //All moving platforms
    public static final Family movingPlatformFamily = Family.all(MovementComponent.class, PlatformComponent.class).get();

    //All moving entities except platforms
    public static final Family movingNonPlatformFamily = Family.all(MovementComponent.class).exclude(PlatformComponent.class).get();

    //All moving mobs except flying mobs
    public static final Family movingGravityMobFamily = Family.all(GravityComponent.class, MovementComponent.class, MobComponent.class).get();

    //All flying mobs
    public static final Family movingNonGravityMobFamily = Family.all(MovementComponent.class, MobComponent.class).exclude(GravityComponent.class).get();

    //All (moving) player(s)
    public static final Family playerFamily = Family.all(MovementComponent.class, PlayerComponent.class).get();

    //All entities (with a position)
    public static final Family allFamily = Family.all(PositionComponent.class).get();
    // all enemies, the player and destructible objects
    public static final Family combatFamily = Family
            .all(PositionComponent.class, StatsComponent.class, GraphicsComponent.class).get();

    // all cameras
    public static final Family cameraFamily = Family
            .all(CameraComponent.class).get();
}
