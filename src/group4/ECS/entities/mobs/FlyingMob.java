package group4.ECS.entities.mobs;

import group4.ECS.components.SplinePathComponent;
import group4.ECS.components.identities.FlyingMobComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.movement.MovementHandlers.FlyingMobMovementHandler;
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
        this.add(new SplinePathComponent(spline, new Vector3f(3, 3, 0), new Vector3f(3, 2, 0), 0, 100));
    }

    public String getName() {
        return "FlyingMob";
    }
}
