package group4.ECS.entities.bullets;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.BulletComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.collision.CollisionHandlers.BulletCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class Projectile extends Entity {

    /**
     * Creates a bullet
     *
     * @param position          left-bottom-back corner of the cuboid representing the bullet
     * @param velocityDirection direction (normalized) of velocity vector
     * @param origin            origin of the Projectile, i.e. weapon
     */
    public Projectile(Vector3f position, Vector3f velocityDirection, Vector3f dimension, Entity origin,
                      int damage, Texture texture) {

        // velocity range
        Vector3f velocityRange = new Vector3f(5.5f, 5.5f, 0.0f);

        // magnitude of velocity vector
        float velocityMagnitude = 5.5f;

        // shader
        Shader shader = Shader.SIMPLE;

        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new DamageComponent(damage, origin));
        this.add(new MovementComponent(velocityDirection.normalized().scale(velocityMagnitude), velocityRange));
        this.add(new GraphicsComponent(shader, texture, dimension, false));
        this.add(new BulletComponent());
        this.add(new CollisionComponent(BulletCollision.getInstance()));

        // add bullet to engine on construction
        TheEngine.getInstance().addEntity(this);

    }

    public static String getName() {
        return "Projectile";
    }

}
