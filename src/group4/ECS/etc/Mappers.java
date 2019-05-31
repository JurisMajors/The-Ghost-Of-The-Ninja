package group4.ECS.etc;

import com.badlogic.ashley.core.ComponentMapper;
import group4.ECS.components.*;
import group4.ECS.components.events.TickComponent;
import group4.ECS.components.events.TimedComponent;
import group4.ECS.components.identities.CameraComponent;
import group4.ECS.components.identities.ConsumableComponent;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.identities.PlayerComponent;
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

    public static final ComponentMapper<TickComponent> tickMapper =
            ComponentMapper.getFor(TickComponent.class);

    public static final ComponentMapper<TimedComponent> timedMapper =
            ComponentMapper.getFor(TimedComponent.class);

    public static final ComponentMapper<ItemComponent> itemMapper =
            ComponentMapper.getFor(ItemComponent.class);
}
