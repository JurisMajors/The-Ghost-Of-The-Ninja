package group4.ECS.systems.animation;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.AnimationComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.etc.EntityState;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;
import group4.utils.DebugUtils;

public class AnimationSystem extends IteratingSystem {
    float deltaTime;
    public AnimationSystem(int priority) {
        super(Families.animationFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.deltaTime = deltaTime;

        if (entity instanceof HierarchicalPlayer) {
            animatePlayer((HierarchicalPlayer) entity);
        }
    }

    private void animatePlayer(HierarchicalPlayer player) {
        AnimationComponent ac = Mappers.animationMapper.get(player);
        System.out.println(ac);
        ac.setAnimation(player.getState());
        ac.getCurrentAnimation().update(this.deltaTime);
//        Vector3f[] walkSplinePoints = new Vector3f[]{
//                new Vector3f(0.0f, 0.0f, 0.0f),
//                new Vector3f(-.5f, 0.0f, 0.0f),
//                new Vector3f(-2.f, 0.0f, 0.0f),
//                new Vector3f(-.1f, 0.2f, 0.0f),
//                new Vector3f(0.1f, 0.2f, 0.0f),
//                new Vector3f(0.7f, 0.0f, 0.0f),
//                new Vector3f(0.5f, 0.0f, 0.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f)
//        };
//        MultiSpline walkSpline = new MultiSpline(walkSplinePoints);

//            for (int i = 0; i < walkSplinePoints.length; i++) {
//                walkSplinePoints[i].addi(
//                        entity.getComponent(PositionComponent.class).position.add(
//                                new Vector3f(
//                                        entity.getComponent(DimensionComponent.class).dimension.x / 2,
//                                        0.0f,
//                                        0.0f)
//
//                        )
//                );
//            }

//        DebugUtils.drawSpline(walkSplinePoints);

        for (IKEndEffector handle : player.IKHandles) {
//            handle.t += deltaTime;
//            handle.t %= 1.0f; //2.0f * Math.PI; // Rotate in radians.
//
//            Vector3f unitCircle;
//
//            if (handle.label == "foot_L") {
//                unitCircle = walkSpline.getPoint((handle.t + 0.5f) % 1.0f);//new Vector3f((float) Math.cos(handle.t) * 0.75f, (float) Math.sin(handle.t) * 0.75f, 0.0f);
//            } else { // "foot_R"
//                unitCircle = walkSpline.getPoint(handle.t);//new Vector3f((float) Math.cos(handle.t) * 0.9f, (float) Math.sin(handle.t) * 0.9f, 0.0f);
//            }

            // Move handle.endPos to be relative of the player position
            PositionComponent pc = Mappers.positionMapper.get(handle);
            pc.position.add(new Vector3f(player.getComponent(DimensionComponent.class).dimension.x / 2, 0.0f, 0.0f));
            // Get the length of the body parts involved in the IK system
            float upperLength = handle.upper.getComponent(DimensionComponent.class).dimension.y;
            float lowerLength = handle.lower.getComponent(DimensionComponent.class).dimension.y;

            // Calculate and set the new rotations for the limbs based on the start and endpoints of the IK chain
            float[] angles = player.getLimbAngles(player.getHipOffset(), pc.position, upperLength, lowerLength, true);
            handle.upper.rotation = angles[0];
            handle.lower.rotation = angles[1];
        }
    }
}