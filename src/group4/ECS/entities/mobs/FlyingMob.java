package group4.ECS.entities.mobs;

import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.AnimationComponent;
import group4.ECS.components.identities.FlyingMobComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.EntityState;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.animation.FrameAnimation;
import group4.ECS.systems.movement.MovementHandlers.FlyingMobMovementHandler;
import group4.graphics.ImageSequence;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

public class FlyingMob extends Mob {

    /**
     * Creates a flying mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public FlyingMob(Vector3f position, Level l) {
        super(position, l, FlyingMobMovementHandler.getInstance());

        // limit the velocity of the flying mob to prevent shaking of the texture
        MovementComponent mc = Mappers.movementMapper.get(this);
        mc.velocityRange = new Vector3f(0.03f, 0.03f, 0);

        this.add(new FlyingMobComponent());
        Vector3f[] splinePoints = new Vector3f[]{
                new Vector3f(0, 1, 0), new Vector3f(0, 1.5f, 0), new Vector3f(1, 2, 0), new Vector3f(1.5f, 2, 0),
                new Vector3f(1.5f, 2, 0), new Vector3f(2, 2, 0), new Vector3f(3, 1.5f, 0), new Vector3f(3, 1, 0),
                new Vector3f(3, 1, 0), new Vector3f(3, 0.5f, 0), new Vector3f(2, 0, 0), new Vector3f(1.5f, 0, 0),
                new Vector3f(1.5f, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 0.5f, 0), new Vector3f(0, 1, 0)
        };
        MultiSpline spline = new MultiSpline(splinePoints);

        // uncomment this and put a flying mob in the level to see spline pathing in action
//        this.add(new SplinePathComponent(spline, new Vector3f(3, 3, 0), new Vector3f(3, 2, 0), 0, 100));
    }

    public FlyingMob(Vector3f position, Level l, Texture texture, float[] texCoords) {
        super(position, l, FlyingMobMovementHandler.getInstance());

        Vector3f bigBatDimension = new Vector3f(1.75f, 1.75f, 0.0f);
        this.getComponent(DimensionComponent.class).dimension = bigBatDimension; // Update size
        // limit the velocity of the flying mob to prevent shaking of the texture
        MovementComponent mc = Mappers.movementMapper.get(this);
        mc.velocityRange = new Vector3f(0.03f, 0.03f, 0);
        this.add(new FlyingMobComponent());
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.DEBUG, bigBatDimension, false));
        this.add(new AnimationComponent());
        this.setupAnimation();
    }

    private void setupAnimation() {
        AnimationComponent ac = this.getComponent(AnimationComponent.class);
        ac.addAnimation(
                EntityState.DEFAULT, // A default animation
                new FrameAnimation(this, 0.0f, ImageSequence.BAT2, 90)
        );
        ac.setAnimation(EntityState.DEFAULT);
    }

    public static String getName() {
        return "FlyingMob";
    }
}
