package group4.ECS.entities.mobs;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class MobBullet extends Entity {
    protected Vector3f dimension = new Vector3f(0.1f, 0.1f, 0.0f);

    /**
     * Creates a bullet
     *
     * @param position          left-bottom-back corner of the cuboid representing the bullet
     * @param velocityDirection direction (normalized) of velocity vector
     */
    public MobBullet(Vector3f position, Vector3f velocityDirection) {
        Vector3f velocityRange = new Vector3f(0.25f, 0.25f, 0.0f);//velocity range
        float velocityMagnitude = 0.25f;//magnitude of velocity vector
        Shader shader = Shader.SIMPLE;//shader
        Texture texture = Texture.DEBUG;//texture

        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(velocityDirection.scale(velocityMagnitude), velocityRange));
        this.add(new GraphicsComponent(shader, texture, dimension));
        this.add(new BulletComponent());
        this.add(new ColliderComponent());
    }
}
