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
import group4.ECS.systems.collision.CollisionHandlers.MobCollision;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class Mob extends Entity {
    //dimension of the mob, aka bounding box
    protected Vector3f dimension = new Vector3f(1.0f, 1.0f, 0.0f);//dimension of the mob, aka bounding box
    public Level level;
    /**
     * Creates a mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public Mob(Vector3f position, Level l, AbstractMovementHandler handler) {
        Vector3f velocityRange = new Vector3f(0.05f, 0.25f, 0.0f);//velocity range
        Shader shader = Shader.SIMPLE;//shader
        Texture texture = Texture.EXIT;//texture
        this.level = l;
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(new Vector3f(), velocityRange));
        this.add(new GravityComponent());
        this.add(new GraphicsComponent(shader, texture, dimension));
        this.add(new CollisionComponent(MobCollision.getInstance()));
        this.add(new HealthComponent(30));
        this.add(new MobComponent(handler));
    }

    public static String getName() {
        return "Mob";
    }
}
