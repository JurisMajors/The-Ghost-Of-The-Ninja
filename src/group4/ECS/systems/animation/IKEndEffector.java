package group4.ECS.systems.animation;

import group4.ECS.entities.BodyPart;
import group4.maths.Vector3f;

public class IKEndEffector {
    public Vector3f startPos, endPos;
    public BodyPart upper, lower;
    public String label;
    public float t;

    public IKEndEffector (BodyPart upper, BodyPart lower, Vector3f startPos, Vector3f endPos, String label) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.upper = upper;
        this.lower = lower;
        this.label = label;
        t = 0.0f;
    }
}
