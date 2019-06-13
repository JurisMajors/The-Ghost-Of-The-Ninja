package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

import java.util.LinkedList;

public class PathComponent implements Component {
    public Integer vertex;
    public LinkedList<Vector3f> coordinates = new LinkedList<Vector3f>();
    public LinkedList<Integer> vertexID = new LinkedList<Integer>();

    public PathComponent() {
    }
}
