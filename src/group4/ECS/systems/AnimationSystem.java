package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.etc.Families;
import group4.game.IKEndEffector;
import group4.maths.Vector3f;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() { super(Families.animationFamily); }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (entity instanceof HierarchicalPlayer) {
            for (IKEndEffector handle : ((HierarchicalPlayer) entity).IKHandles) {
                handle.t += deltaTime / 200;
                handle.t %= 2.0f * Math.PI; // Rotate in radians.

                Vector3f unitCircle;

                if (handle.label == "foot_L") {
                    unitCircle = new Vector3f((float) Math.cos(handle.t - 1) * 0.75f, (float) Math.sin(handle.t - 1) * 0.75f, 0.0f);
                } else {
                    unitCircle = new Vector3f((float) Math.cos(handle.t) * 0.9f, (float) Math.sin(handle.t) * 0.9f, 0.0f);
                }

                handle.endPos = unitCircle.add(new Vector3f(entity.getComponent(DimensionComponent.class).dimension.x / 2, 0.8f, 0.0f));

                float upperLength = handle.upper.getComponent(DimensionComponent.class).dimension.y;
                float lowerLength = handle.lower.getComponent(DimensionComponent.class).dimension.y;;
                float[] angles = ((HierarchicalPlayer) entity).getLimbAngles(handle.startPos, handle.endPos, upperLength, lowerLength);
                handle.upper.rotation = angles[0];
                handle.lower.rotation = angles[1];
            }
        }
    }
}
