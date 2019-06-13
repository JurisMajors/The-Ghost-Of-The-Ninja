package group4.ECS.entities.mobs;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.AnimationComponent;
import group4.ECS.components.identities.FlyingMobComponent;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.items.weapons.MobRangedAttack;
import group4.ECS.etc.EntityConst;
import group4.ECS.etc.EntityState;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.animation.FrameAnimation;
import group4.ECS.systems.collision.CollisionHandlers.MobCollision;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;
import group4.ECS.systems.movement.MovementHandlers.FlyingMobMovementHandler;
import group4.graphics.ImageSequence;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

public class FlyingMob extends Mob {

    protected Vector3f dimension = new Vector3f(1.0f, 1.0f, 0.0f); //dimension of the mob, aka bounding box

    /**
     * Creates a mob
     *
     * @param position left-bottom-back corner of the cuboid representing the mob
     */
    public FlyingMob(Vector3f position, Level l, AbstractMovementHandler handler) {
        Vector3f velocityRange = new Vector3f(0.05f, 0.25f, 0.0f);//velocity range
        this.level = l;
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new MovementComponent(new Vector3f(), velocityRange));
        this.add(new GravityComponent());
        this.add(new CollisionComponent(MobCollision.getInstance()));
        this.add(new HealthComponent(30));

        // limit the velocity of the flying mob to prevent shaking of the texture
        MovementComponent mc = Mappers.movementMapper.get(this);
        mc.velocityRange = new Vector3f(0.03f, 0.03f, 0);
        float attackRange = 5.0f;

        // TODO; more sensible wpn for a bat plz
        this.wpn = new MobRangedAttack(5, 2.0f,
                new Vector3f(1f, 1f, 0), new Vector3f(1f, 1f, 0f), Texture.PROJECTILE);
        this.add(new MobComponent(handler, attackRange, wpn));
    }

    public FlyingMob(Vector3f position, Level l) {
        this(position, l, FlyingMobMovementHandler.getInstance());
        this.remove(GraphicsComponent.class);
        Vector3f bigBatDimension = new Vector3f(1.75f, 1.75f, 0.0f);
        this.getComponent(DimensionComponent.class).dimension = bigBatDimension; // Update size

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
