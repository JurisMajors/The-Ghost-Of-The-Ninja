package group4.ECS.entities.mobs;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class MobBullet extends Entity {
    protected Vector3f dimension = new Vector3f(0.1f, 0.1f, 0.0f);

    public MobBullet(Vector3f position, Vector3f direction) {
        Vector3f velocityRange = new Vector3f(0.25f, 0.25f, 0.0f);
        float speed=0.25f;
        Shader shader = Shader.SIMPLE;
        Texture texture = Texture.DEBUG;
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(direction.scale(speed), velocityRange));
        this.add(new GraphicsComponent(shader, texture, dimension));
        this.add(new BulletComponent());
        this.add(new ColliderComponent());
    }
}
