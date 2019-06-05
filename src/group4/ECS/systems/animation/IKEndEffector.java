package group4.ECS.systems.animation;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.BodyPart;
import group4.maths.Vector3f;

public class IKEndEffector extends Entity {
    public BodyPart upper, lower;
    public String label;
    public float t;

    public IKEndEffector(BodyPart upper, BodyPart lower, Vector3f endPos, String label) {
        this.upper = upper;
        this.lower = lower;
        this.label = label;
        t = 0.0f;

        this.add(new PositionComponent(endPos));
    }
}
