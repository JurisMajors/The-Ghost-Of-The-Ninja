package group4.ECS.entities.hazards;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.systems.collision.CollisionHandlers.DamageCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class Spikes extends Entity {

    // determines in which direction the spikes point
    public Vector3f normal;

    /**
     * @param position
     * @param shader
     * @param texture
     * @param texCoords
     * @param normal the direction of the spikes
     */
    public Spikes(Vector3f position, Shader shader, Texture texture, float[] texCoords, Vector3f normal) {

        this.normal = normal;
        int damage = 10;
        Set<Class<? extends Entity>> excluded = new HashSet<>();

        // ghost shouldn't take dmg from spikes in the first place
        excluded.add(Ghost.class);

        // Construct vertex array
        float[] vertices = new float[] {
                -0.15f, -0.15f, 0,
                -0.15f, 0.85f, 0,
                0.85f, 0.85f, 0,
                0.85f, -0.15f, 0,
        };

        // Construct index array (used for geometry mesh)
        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        this.add(new PositionComponent(position.add(new Vector3f(0.15f, 0.15f, 0.0f))));
        this.add(new CollisionComponent(DamageCollision.getInstance()));
        this.add(new DamageComponent(damage, excluded, this));
        this.add(new GraphicsComponent(shader, texture, vertices, indices, texCoords));
        this.add(new DimensionComponent(new Vector3f(0.7f, 0.7f, 0.0f)));
    }

    public static String getName() {
        return "Spikes";
    }

}
