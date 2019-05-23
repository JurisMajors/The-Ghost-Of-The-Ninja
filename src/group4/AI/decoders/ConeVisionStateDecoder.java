package group4.AI.decoders;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.stats.MovementComponent;
import group4.maths.Vector3f;

/**
 * Starts the ray casting on an angle, relative to the velocity of the ghost
 */
public class ConeVisionStateDecoder extends RayStateDecoder{

    public ConeVisionStateDecoder(int n, float angleRange) {
        super(n, angleRange);
    }

    @Override
    Vector3f getCastingStart(Entity ghost) {
        Vector3f ghostVel = ghost.getComponent(MovementComponent.class).velocity.normalized();
        Vector3f start = ghostVel.rotateXY(-1 * this.angleRange / 2.0f);
        start.normalizei();
        return start;
    }
}
