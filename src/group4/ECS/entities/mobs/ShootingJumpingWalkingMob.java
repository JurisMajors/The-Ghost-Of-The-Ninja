package group4.ECS.entities.mobs;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class ShootingJumpingWalkingMob extends Entity {
    protected Vector3f dimension = new Vector3f(1.0f, 1.0f, 0.0f);//dimension of the mob, aka bounding box

    /**
     * Creates a shooting jumping&walking mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public ShootingJumpingWalkingMob(Vector3f position) {
        Vector3f velocityRange = new Vector3f(0.05f, 0.25f, 0.0f);//velocity range
        Shader shader = Shader.SIMPLE;//shader
        Texture texture = Texture.DEBUG;//texture

        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(new Vector3f(), velocityRange));
        this.add(new GravityComponent());
        this.add(new GraphicsComponent(shader, texture, dimension));
        this.add(new ShootingComponent(new Vector3f(0.5f, 2.0f, 0.0f), 150));
        this.add(new JumpingWalkingMobComponent());
        this.add(new ColliderComponent());
    }
}
