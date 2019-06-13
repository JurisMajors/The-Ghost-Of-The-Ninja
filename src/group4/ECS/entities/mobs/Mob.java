package group4.ECS.entities.mobs;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.items.weapons.MobMeleeAttack;
import group4.ECS.systems.collision.CollisionHandlers.MobCollision;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class Mob extends Entity {

    public Level level;
    public Entity wpn;
    public float attackRange;

    public static String getName() {
        return "Mob";
    }
}
