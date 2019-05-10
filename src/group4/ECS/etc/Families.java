package group4.ECS.etc;

import com.badlogic.ashley.core.Family;
import group4.ECS.components.*;

/**
 * This class determines groups (families) of entities which share the same components
 * such that systems can easily retrieve corresponding entities
 */
public class Families {

    public static final Family platformFamily = Family.all(PlatformComponent.class).get();
    public static final Family movingPlatformFamily = Family.all(MovementComponent.class, PlatformComponent.class).get();
    public static final Family movingNonPlatformFamily = Family.all(MovementComponent.class).exclude(PlatformComponent.class).get();
    public static final Family movingGravityMobFamily = Family.all(MovementComponent.class, MobComponent.class).get();
    public static final Family movingNonGravityMobFamily = Family.all(MovementComponent.class, MobComponent.class).exclude(GravityComponent.class).get();
    public static final Family playerFamily = Family.all(MovementComponent.class, PlayerComponent.class).get();
    public static final Family allFamily = Family.all(PositionComponent.class).get();
}
