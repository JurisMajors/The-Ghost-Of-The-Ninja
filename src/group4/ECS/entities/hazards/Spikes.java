package group4.ECS.entities.hazards;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.systems.collision.CollisionHandlers.MeleeCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class Spikes extends Entity {

    /**
     * @param position
     * @param shader
     * @param texture
     * @param texCoords
     */
    public Spikes(Vector3f position, Shader shader, Texture texture, float[] texCoords) {

        int damage = 10;
        Set<Class<? extends Entity>> excluded = new HashSet<>();

        // Construct vertex array
        float[] vertices = new float[] {
                0, 0, 0,
                0, 1.0f, 0,
                1.0f, 1.0f, 0,
                1.0f, 0, 0,
        };

        // Construct index array (used for geometry mesh)
        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        this.add(new PositionComponent(position));
        this.add(new CollisionComponent(MeleeCollision.getInstance()));
        this.add(new DamageComponent(damage, excluded));
        this.add(new GraphicsComponent(shader, texture, vertices, indices, texCoords));
        this.add(new DimensionComponent());
    }

    public static String getName() {
        return "Spikes";
    }

}
