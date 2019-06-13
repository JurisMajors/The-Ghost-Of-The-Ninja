package group4.ECS.entities.AStarMobs;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PathComponent;
import group4.ECS.components.identities.AStarMobComponent;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.systems.GraphHandlers.AbstractGraphHandler;
import group4.ECS.systems.collision.CollisionHandlers.MobCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class AStarMob extends Mob {
    protected Vector3f dimension = new Vector3f(1.0f, 1.0f, 0.0f);

    public AStarMob(Vector3f position, Level l, AbstractGraphHandler handler, GraphComponent graphComponent,
                    float attackRange, Entity weapon) {
        Vector3f velocityRange = new Vector3f(0.05f, 0.25f, 0.0f);
        Shader shader = Shader.SIMPLE;
        Texture texture = Texture.EXIT;
        this.level = l;
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(new Vector3f(), velocityRange));
        this.add(new GravityComponent());
        this.add(new CollisionComponent(MobCollision.getInstance()));
        this.add(new GraphicsComponent(shader, texture, dimension, false));
        this.add(new HealthComponent(30));

        this.add(graphComponent);

        this.add(new AStarMobComponent(handler));
        this.add(new PathComponent());
        this.wpn = weapon;
        this.attackRange = attackRange;
        this.add(new MobComponent(null, attackRange, weapon));
    }

    public AStarMob(Vector3f position, Level l, Texture tex, float[] texCoord, AbstractGraphHandler handler,
                    GraphComponent graphComponent, float attackRange, Entity weapon) {
        this(position, l, handler, graphComponent, attackRange, weapon);
        this.remove(GraphicsComponent.class);
        this.add(new GraphicsComponent(Shader.SIMPLE, tex, dimension, texCoord, false));

    }

    public static String getName() {
        return "Mob";
    }
}
