package group4.AI.decoders;

import com.badlogic.ashley.core.Entity;
import group4.maths.Vector3f;

/**
 * Casts rays around the ghost
 */
public class CircleVisionStateDecoder extends RayStateDecoder implements StateDecoderInterface {
    private float gap;
    /**
     * @param n number of rays to be casted
     * @param angleGap the gap in degrees between the first and the last ray
     *
     */
    public CircleVisionStateDecoder(int n, float angleGap) {
        super(n, 360.0f - angleGap);
        this.gap = angleGap / 2.0f;
    }

    @Override
    Vector3f getCastingStart(Entity ghost) {
        // vector representing the x-axis unit vector, which is at 0 degrees
        Vector3f start = new Vector3f(1, 0, 0);
        // rotate downwards considering the gap
        start.rotateXYi(-90 + this.gap);
        // normalize just for safety
        start.normalizei();
        return start;
    }
}
