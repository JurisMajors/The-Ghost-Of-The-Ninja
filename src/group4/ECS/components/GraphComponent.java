package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphComponent implements Component {
    public Module module;
    public String inFile;
    public String outFile;
    public int vertexID;
    public ArrayList<Vector3f> vertexCoords;
    public ArrayList<ArrayList<Vector3f>> edgePath;
    public ArrayList<ArrayList<Integer>> neighbours;
    public int[][] edgeID;

    public TreeMap<Float, Integer> yHValue;

    public GraphComponent(Module module) {
        this.module = module;
    }

    public GraphComponent(Module module, String outFile) {
        this.module = module;
        this.outFile = outFile;
    }

    public GraphComponent(String inFile) {
        this.inFile = inFile;
    }

}
