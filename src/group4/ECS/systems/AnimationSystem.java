package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.etc.Families;
import group4.game.IKEndEffector;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;
import group4.maths.spline.Spline;
import group4.utils.DebugUtils;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem(int priority) {
        super(Families.animationFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (entity instanceof HierarchicalPlayer) {

            Vector3f[] walkSplinePoints = new Vector3f[]{
                    new Vector3f(0.0f, 0.0f, 0.0f),
                    new Vector3f(-.5f, 0.0f, 0.0f),
                    new Vector3f(-2.f, 0.0f, 0.0f),
                    new Vector3f(-.1f, 0.2f, 0.0f),
                    new Vector3f(0.1f, 0.2f, 0.0f),
                    new Vector3f(0.7f, 0.0f, 0.0f),
                    new Vector3f(0.5f, 0.0f, 0.0f),
                    new Vector3f(0.0f, 0.0f, 0.0f)
            };
            MultiSpline walkSpline = new MultiSpline(walkSplinePoints);

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

            DebugUtils.drawSpline(walkSplinePoints);

            for (IKEndEffector handle : ((HierarchicalPlayer) entity).IKHandles) {
                handle.t += deltaTime / 60;
                handle.t %= 1.0f; //2.0f * Math.PI; // Rotate in radians.

                float upperLength = handle.upper.getComponent(DimensionComponent.class).dimension.y;
                float lowerLength = handle.lower.getComponent(DimensionComponent.class).dimension.y;

                Vector3f unitCircle;
                float[] angles;

                if (handle.label == "foot_L") {
                    unitCircle = new Vector3f((float) Math.cos(handle.t - 1) * 0.75f, (float) Math.sin(handle.t - 1) * 0.75f, 0.0f);
                    handle.endPos = unitCircle.add(((HierarchicalPlayer) entity).getHipOffset());
                    angles = ((HierarchicalPlayer) entity).getLimbAngles(((HierarchicalPlayer) entity).getHipOffset(), handle.endPos, upperLength, lowerLength, true);
                } else if (handle.label == "foot_R") {
                    unitCircle = new Vector3f((float) Math.cos(handle.t) * 0.87f, (float) Math.sin(handle.t) * 0.87f, 0.0f);
                    handle.endPos = unitCircle.add(((HierarchicalPlayer) entity).getHipOffset());
                    angles = ((HierarchicalPlayer) entity).getLimbAngles(((HierarchicalPlayer) entity).getHipOffset(), handle.endPos, upperLength, lowerLength, true);
                } else {
                    unitCircle = new Vector3f((float) Math.cos(handle.t - 1.75) * 0.87f, (float) Math.sin(handle.t - 1.75) * 0.87f, 0.0f);
                    handle.endPos = unitCircle.add(((HierarchicalPlayer) entity).getShoulderPosition());
                    angles = ((HierarchicalPlayer) entity).getLimbAngles(((HierarchicalPlayer) entity).getShoulderPosition(), handle.endPos, upperLength, lowerLength, false);
                }
                System.out.println(unitCircle);
                // Scale slightly inwards to fix flashing issue (caused by arccos(x) where x>1)
                unitCircle.scalei(0.99f);

                handle.upper.rotation = angles[0];
                handle.lower.rotation = angles[1];
            }
        }
    }
}
