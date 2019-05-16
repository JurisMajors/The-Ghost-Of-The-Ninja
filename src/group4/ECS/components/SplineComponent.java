package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class SplineComponent implements Component {

    public Vector3f[] points;
    public Vector3f[] normals;
    public float thickness;

    public SplineComponent(Vector3f[] points, Vector3f[] normals, float thickness) {
        this.points = points;
        this.normals = normals;
        this.thickness = thickness;
    }
}
