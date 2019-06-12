package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.AnimationComponent;
import group4.ECS.components.identities.CoinComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.etc.EntityState;
import group4.ECS.systems.animation.FrameAnimation;
import group4.ECS.systems.collision.CollisionHandlers.CoinCollision;
import group4.graphics.ImageSequence;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class Torch extends Entity {
    /**
     * Creates a coin with score value value.
     *
     * @param position  bottom left position of the coin
     */
    public Torch(Vector3f position) {
        Vector3f defaultDimension = new Vector3f(1.0f, 2.0f, 0.0f);
        this.add(new PositionComponent(position));

        // This is a temp new one to illustrate the animation
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.DEBUG, defaultDimension, true));
        this.add(new AnimationComponent());
        this.setupAnimation();
    }

    private void setupAnimation() {
        AnimationComponent ac = this.getComponent(AnimationComponent.class);
        ac.addAnimation(
                EntityState.DEFAULT, // A default animation
                new FrameAnimation(this, 0.0f, ImageSequence.TORCH, 30)
        );
        ac.setAnimation(EntityState.DEFAULT);
    }
    public static String getName() {
        return "Torch";
    }
}
