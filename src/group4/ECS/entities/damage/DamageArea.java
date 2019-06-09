package group4.ECS.entities.damage;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.events.Event;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.collision.CollisionHandlers.MeleeCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

import java.util.Set;

public class DamageArea extends Entity {

    public Event event;

    /**
     * This is a damage area specifically for melee, which takes the origin of damage
     * to prevent self hitting
     *
     * @param position  position of the damage area
     * @param dimension dimension field of effect
     * @param damage    damage inflicted on colliding entity (HealthComponent needed)
     * @param excluded  exclude for damage
     */
    public DamageArea(Vector3f position, Vector3f dimension, int damage, Set<Class<? extends Entity>> excluded,
                      int duration) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new CollisionComponent(MeleeCollision.getInstance()));
        this.add(new DamageComponent(damage, excluded));

        // Construct vertex array
        float[] vertices = new float[] {
                0, 0, 0,
                0, dimension.y, 0,
                dimension.x, dimension.y, 0,
                dimension.x, 0, 0,
        };

        // Construct index array (used for geometry mesh)
        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        // Construct texture coords
        float[] tcs = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        Shader shader = Shader.SIMPLE;
        Texture texture = Texture.HITBOX;

        this.add(new GraphicsComponent(shader, texture, vertices, indices, tcs));

        // add entity to engine on construction
        TheEngine.getInstance().addEntity(this);

         event = new Event(this, duration,
                 (entity, dur, passed) -> {
             if (passed == dur) {
                 TheEngine.getInstance().removeEntity(entity);
             }
         })
         ;

    }

}
