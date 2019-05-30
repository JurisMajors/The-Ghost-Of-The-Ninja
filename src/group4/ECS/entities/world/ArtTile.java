package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.systems.collision.CollisionHandlers.ExitCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class ArtTile extends Entity {

    /**
     * Simple tile for displaying art which does not have to deal with collisions.
     *
     * @param position Vector3f, where to place the tile
     * @param dimension Vector3f, the size of the tile
     * @param shader Shader, the material to use
     * @param texture Texture, the image to load into the material (Likely a tilemap)
     * @param texCoords Float[], st-coordinates within the Texture
     */
    public ArtTile(Vector3f position, Vector3f dimension, Shader shader, Texture texture, float[] texCoords) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new GraphicsComponent(shader, texture, dimension, texCoords, false));
    }

    public static String getName() {
        return "ArtTile";
    }
}
