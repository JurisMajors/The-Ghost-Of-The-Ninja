package group4.ECS.etc;

import com.badlogic.ashley.core.ComponentMapper;
import group4.ECS.components.*;
import group4.ECS.components.events.EventComponent;
import group4.ECS.components.identities.*;
import group4.ECS.components.physics.*;
import group4.ECS.components.stats.*;

/**
 * Mappers retrieve components from entities in O(1) time
 * use e.g. positionMapper.get(entity) to retrieve position data from entity
 */
public class Mappers {

    public static final ComponentMapper<PositionComponent> positionMapper =
            ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<DimensionComponent> dimensionMapper =
            ComponentMapper.getFor(DimensionComponent.class);

    public static final ComponentMapper<PhysicsComponent> physicsMapper =
            ComponentMapper.getFor(PhysicsComponent.class);

    public static final ComponentMapper<GraphicsComponent> graphicsMapper =
            ComponentMapper.getFor(GraphicsComponent.class);

    public static final ComponentMapper<MovementComponent> movementMapper =
            ComponentMapper.getFor(MovementComponent.class);

    public static final ComponentMapper<ConsumableComponent> consumableMapper =
            ComponentMapper.getFor(ConsumableComponent.class);

    public static final ComponentMapper<HealthComponent> dieableMapper =
            ComponentMapper.getFor(HealthComponent.class);

    public static final ComponentMapper<AudioComponent> audioMapper =
            ComponentMapper.getFor(AudioComponent.class);

    public static final ComponentMapper<CameraComponent> cameraMapper =
            ComponentMapper.getFor(CameraComponent.class);

    public static final ComponentMapper<GravityComponent> gravityMapper =
            ComponentMapper.getFor(GravityComponent.class);

    public static final ComponentMapper<RangeComponent> rangeMapper =
            ComponentMapper.getFor(RangeComponent.class);

    public static final ComponentMapper<WeaponComponent> weaponMapper =
            ComponentMapper.getFor(WeaponComponent.class);

    public static final ComponentMapper<RangeWeaponComponent> rangeWeaponMapper =
            ComponentMapper.getFor(RangeWeaponComponent.class);

    public static final ComponentMapper<MeleeWeaponComponent> meleeWeaponMapper =
            ComponentMapper.getFor(MeleeWeaponComponent.class);

    public static final ComponentMapper<CollisionComponent> collisionMapper =
            ComponentMapper.getFor(CollisionComponent.class);

    public static final ComponentMapper<SplineComponent> splineMapper =
            ComponentMapper.getFor(SplineComponent.class);

    public static final ComponentMapper<SplinePathComponent> splinePathMapper =
            ComponentMapper.getFor(SplinePathComponent.class);

    public static final ComponentMapper<MobComponent> mobMapper =
            ComponentMapper.getFor(MobComponent.class);

    public static final ComponentMapper<DamageComponent> damageMapper =
            ComponentMapper.getFor(DamageComponent.class);

    public static final ComponentMapper<PlayerComponent> playerMapper =
            ComponentMapper.getFor(PlayerComponent.class);

    public static final ComponentMapper<HealthComponent> healthMapper =
            ComponentMapper.getFor(HealthComponent.class);

    public static final ComponentMapper<EventComponent> eventMapper =
            ComponentMapper.getFor(EventComponent.class);

    public static final ComponentMapper<ItemComponent> itemMapper =
            ComponentMapper.getFor(ItemComponent.class);


    public static final ComponentMapper<CoinComponent> coinMapper =
            ComponentMapper.getFor(CoinComponent.class);

    public static final ComponentMapper<ScoreComponent> scoreMapper =
            ComponentMapper.getFor(ScoreComponent.class);

    public static final ComponentMapper<AnimationComponent> animationMapper =
            ComponentMapper.getFor(AnimationComponent.class);

    public static final ComponentMapper<AStarMobComponent> aStarMobMapper =
            ComponentMapper.getFor(AStarMobComponent.class);

    public static final ComponentMapper<GraphComponent> graphMapper =
            ComponentMapper.getFor(GraphComponent.class);

    public static final ComponentMapper<PathComponent> pathMapper =
            ComponentMapper.getFor(PathComponent.class);
}
