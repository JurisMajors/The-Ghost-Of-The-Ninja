package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.GraphComponent;
import group4.ECS.components.PathComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

import java.util.*;

public class AStarPathSystem extends IteratingSystem {

    public AStarPathSystem() {
        super(Families.aStarMobFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GraphComponent gc = Mappers.graphMapper.get(entity);
        PathComponent pathc = Mappers.pathMapper.get(entity);
        PositionComponent ppc = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));
        if (gc.vertexID != -1) {
            int playerVertexID = 0;
            for (int i = 0; i < gc.vertexCoords.size(); i++) {
                if (Math.sqrt(Math.pow(ppc.position.x - gc.vertexCoords.get(i).x, 2) + Math.pow(ppc.position.y - gc.vertexCoords.get(i).y, 2)) < Math.sqrt(Math.pow(ppc.position.x - gc.vertexCoords.get(playerVertexID).x, 2) + Math.pow(ppc.position.y - gc.vertexCoords.get(playerVertexID).y, 2))) {
                    playerVertexID = i;
                }
            }
            if (Math.sqrt(Math.pow(mc.velocityRange.x, 2) + Math.pow(mc.velocityRange.y, 2)) >= Math.sqrt(Math.pow(ppc.position.x - gc.vertexCoords.get(playerVertexID).x, 2) + Math.pow(ppc.position.y - gc.vertexCoords.get(playerVertexID).y, 2))) {
                computePath(entity, gc.vertexID, playerVertexID);
            }
        }
    }

    private void computePath(Entity entity, int src, int dest) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        PathComponent pathc = Mappers.pathMapper.get(entity);
        GraphComponent gc = Mappers.graphMapper.get(entity);

        float[] hValue = new float[gc.vertexCoords.size()];
        for (int i = 0; i < hValue.length; i++) {
            int value = (int) (Math.abs(gc.vertexCoords.get(i).x - gc.vertexCoords.get(dest).x) / mc.velocityRange.x);
            if (gc.vertexCoords.get(i).y <= gc.vertexCoords.get(dest).y) {
                value += gc.yHValue.ceilingKey(gc.vertexCoords.get(dest).y - gc.vertexCoords.get(i).y);
            } else value += gc.yHValue.floorKey(gc.vertexCoords.get(i).y - gc.vertexCoords.get(dest).y);
            hValue[i] = value;
        }

        float[] fValue = new float[gc.vertexCoords.size()];
        for (int i = 0; i < fValue.length; i++) fValue[i] = Float.MAX_VALUE;
        fValue[src] = hValue[src];

        int[] parent = new int[gc.vertexCoords.size()];
        for (int i = 0; i < parent.length; i++) parent[i] = -1;

        TreeMap<Float, TreeSet<Integer>> fVertex = new TreeMap<Float, TreeSet<Integer>>();
        for (int i = 0; i < fValue.length; i++) {
            if (!fVertex.containsKey(fValue[i])) fVertex.put(fValue[i], new TreeSet<Integer>());
            fVertex.get(fValue[i]).add(i);
        }

        HashSet<Integer> closed = new HashSet<Integer>();

        boolean exists = false;
        while (fVertex.size() > 0) {
            int vertex = fVertex.get(fVertex.firstKey()).first();
            fVertex.get(fVertex.firstKey()).remove(vertex);
            closed.add(vertex);
            if (vertex == dest) {
                exists = true;
                break;
            }
            if (fVertex.get(fVertex.firstKey()).size() == 0) fVertex.remove(fVertex.firstKey());
            for (int i = 0; i < gc.neighbours.get(vertex).size(); i++) {
                int child = gc.neighbours.get(vertex).get(i);
                if (closed.contains(child)) continue;
                float prev = fValue[child];
                float curr = fValue[vertex] - hValue[vertex] + gc.edgePath.get(gc.edgeID[vertex][child]).size() + hValue[child];
                if (curr < prev) {
                    parent[child] = vertex;
                    fVertex.get(prev).remove(child);
                    if (fVertex.get(prev).size() == 0) fVertex.remove(prev);
                    if (!fVertex.containsKey(curr)) fVertex.put(curr, new TreeSet<Integer>());
                    fVertex.get(curr).add(child);
                    fValue[child] = curr;
                }
            }
        }

        if (exists) {
            int curr = dest;
            pathc.vertexID = new LinkedList<Integer>();
            pathc.coordinates = new LinkedList<Vector3f>();
            while (parent[curr] != -1) {
                pathc.vertexID.addFirst(curr);
                for (int i = gc.edgePath.get(gc.edgeID[parent[curr]][curr]).size() - 1; i >= 0; i--) {
                    pathc.vertexID.addFirst(-1);
                    pathc.coordinates.addFirst(gc.edgePath.get(gc.edgeID[parent[curr]][curr]).get(i));
                }
                pathc.vertexID.removeFirst();
                curr = parent[curr];
            }
        }
    }
}