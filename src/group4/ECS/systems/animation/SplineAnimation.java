package group4.ECS.systems.animation;

import com.badlogic.ashley.core.Entity;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

public class SplineAnimation extends Animation {
    private MultiSpline spline;

    public SplineAnimation(Entity target, float offsetT, Vector3f[] splinePoints) {
        super(target, offsetT);
        spline = new MultiSpline(splinePoints);

    }

    @Override
    protected void stepAnimation() {
        Vector3f point = this.spline.getPoint(this.currentT);//new Vector3f((float) Math.cos(handle.t) * 0.9f, (float) Math.sin(handle.t) * 0.9f, 0.0f);
    }
}
