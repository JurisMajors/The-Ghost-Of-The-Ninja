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
                handle.t += deltaTime;
                handle.t %= 2.0f * Math.PI; // Rotate in radians.

                handle.endPos = new Vector3f((float) Math.cos(handle.t), (float) Math.sin(handle.t), 0.0f);
                if (handle.label == "foot_L") {
                    handle.endPos.addi(new Vector3f(0.33f, 0.0f, 0.0f));
                } else {
                    handle.endPos.addi(new Vector3f(0.66f, 0.0f, 0.0f));
                }

                float upperLength = handle.upper.getComponent(DimensionComponent.class).dimension.y;
                float lowerLength = handle.lower.getComponent(DimensionComponent.class).dimension.y;;
                float[] angles = ((HierarchicalPlayer) entity).getLimbAngles(handle.startPos, handle.endPos, upperLength, lowerLength);
                handle.upper.rotation = angles[0];
                handle.lower.rotation = angles[1];
            }
        }
    }
}
