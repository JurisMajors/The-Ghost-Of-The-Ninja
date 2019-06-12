package group4.ECS.entities.mobs;

import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.AnimationComponent;
import group4.ECS.components.identities.FlappingMobComponent;
import group4.ECS.etc.EntityState;
import group4.ECS.systems.animation.FrameAnimation;
import group4.ECS.systems.movement.MovementHandlers.FlappingMobMovementHandler;
import group4.graphics.ImageSequence;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class FlappingMob extends Mob {

    /**
     * Creates a flapping mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public FlappingMob(Vector3f position, Level l) {
        super(position, l, FlappingMobMovementHandler.getInstance());
        this.add(new FlappingMobComponent());
    }

    public FlappingMob(Vector3f position, Level l, Texture tex, float[] texCoords) {
        super(position, l, FlappingMobMovementHandler.getInstance());
        this.add(new FlappingMobComponent());
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.DEBUG, new Vector3f(1.0f, 1.0f, 0.0f), false));
        this.add(new AnimationComponent());
        this.setupAnimation();
    }

    private void setupAnimation() {
        AnimationComponent ac = this.getComponent(AnimationComponent.class);
        ac.addAnimation(
                EntityState.DEFAULT, // A default animation
                new FrameAnimation(this, 0.0f, ImageSequence.BAT, 120)
        );
        ac.setAnimation(EntityState.DEFAULT);
    }

    public static String getName() {
        return "FlappingMob";
    }
}
