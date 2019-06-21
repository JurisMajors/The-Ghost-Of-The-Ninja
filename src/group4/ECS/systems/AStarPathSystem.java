package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphComponent;
import group4.ECS.components.PathComponent;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.items.weapons.MobMeleeAttack;
import group4.ECS.entities.items.weapons.MobRangedAttack;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.EntityConst;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;
import group4.maths.Vector3f;

import java.util.*;

public class AStarPathSystem extends AbstractMovementHandler {

    public AStarPathSystem(int priority) {
        super(Families.aStarMobFamily, priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GraphComponent gc = Mappers.graphMapper.get(entity);
        PathComponent pathc = Mappers.pathMapper.get(entity);
        PositionComponent ppc = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));
        PositionComponent mobPos = Mappers.positionMapper.get(entity);
        DimensionComponent mobDim = Mappers.dimensionMapper.get(entity);
        MobComponent mobC = Mappers.mobMapper.get(entity);

        if (pathc.vertex == null) initialVertex(entity);

        if (pathc.vertex != -1) {
            setVisionRange(entity);

            // center of the mob
            Vector3f center = mobPos.position.add(mobDim.dimension.scale(0.5f));
            // only cast rays when the player is close enough to the mob
            float distance = getPlayerPosition().euclidDist(center);

            boolean canSee;
            if (mobC.currentVisionRange == MobComponent.chaseRange) {
                canSee = distance <= MobComponent.chaseRange;
            } else {
                canSee = canSeePlayer(mobPos, mobDim, mobC.currentVisionRange);
            }

            if (!canSee) {
                return;
            }
            // TODO: change when new attack range thing
            boolean shouldAttack = canSee && ppc.position.euclidDist(mobPos.position) <= mobC.attackRange; // whether the mob should attack

            if (!shouldAttack) {
                mobC.state = EntityConst.MobState.DEFAULT;
                int playerVertexID = 0;
                for (int i = 0; i < gc.vertexCoords.size(); i++) {
                    if (Math.sqrt(Math.pow(ppc.position.x - gc.vertexCoords.get(i).x, 2) + Math.pow(ppc.position.y - gc.vertexCoords.get(i).y, 2)) < Math.sqrt(Math.pow(ppc.position.x - gc.vertexCoords.get(playerVertexID).x, 2) + Math.pow(ppc.position.y - gc.vertexCoords.get(playerVertexID).y, 2))) {
                        playerVertexID = i;
                    }
                }
                if (Math.sqrt(Math.pow(mc.velocityRange.x, 2) + Math.pow(mc.velocityRange.y, 2)) >= Math.sqrt(Math.pow(ppc.position.x - gc.vertexCoords.get(playerVertexID).x, 2) + Math.pow(ppc.position.y - gc.vertexCoords.get(playerVertexID).y, 2))) {
                    computePath(entity, pathc.vertex, playerVertexID);
                }
            } else {
                if (mobC.weapon instanceof MobMeleeAttack) {
                    mobC.state = EntityConst.MobState.MELEE;
                } else if (mobC.weapon instanceof MobRangedAttack) {
                    mobC.state = EntityConst.MobState.RANGED;
                }
                pathc.vertexID.clear();
                pathc.coordinates.clear();
            }
        }
    }

    public void initialVertex(Entity entity) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        GraphComponent gc = Mappers.graphMapper.get(entity);
        PathComponent pathc = Mappers.pathMapper.get(entity);
        pathc.vertex = 0;
        for (int i = 0; i < gc.vertexCoords.size(); i++) {
            if (Math.sqrt(Math.pow(pc.position.x - gc.vertexCoords.get(i).x, 2) + Math.pow(pc.position.y - gc.vertexCoords.get(i).y, 2))
                    < Math.sqrt(Math.pow(pc.position.x - gc.vertexCoords.get(pathc.vertex).x, 2) + Math.pow(pc.position.y - gc.vertexCoords.get(pathc.vertex).y, 2))) {
                pathc.vertex = i;
            }
        }
        pc.position = gc.vertexCoords.get(pathc.vertex);
    }

    private void computePath(Entity entity, int src, int dest) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        PathComponent pathc = Mappers.pathMapper.get(entity);
        GraphComponent gc = Mappers.graphMapper.get(entity);

        float[] hValue = new float[gc.vertexCoords.size()];
        for (int i = 0; i < hValue.length; i++) {
            int value = (int) (Math.abs(gc.vertexCoords.get(i).x - gc.vertexCoords.get(dest).x) / mc.velocityRange.x);
            if (gc.vertexCoords.get(i).y <= gc.vertexCoords.get(dest).y) {
                value = (int) Math.max(gc.yHValue.ceilingKey(gc.vertexCoords.get(dest).y - gc.vertexCoords.get(i).y), value);
            } else
                value = (int) Math.max(gc.yHValue.floorKey(gc.vertexCoords.get(dest).y - gc.vertexCoords.get(i).y), value);
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
            if(fVertex.firstKey()==Float.MAX_VALUE) break;
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

    public boolean inRange(Mob entity, Vector3f p){
        PositionComponent pc = Mappers.positionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));
        DimensionComponent dc = Mappers.dimensionMapper.get(TheEngine.getInstance().getEntitiesFor(Families.playerFamily).get(0));
        float x=entity.wpn.getComponent(MeleeWeaponComponent.class).hitBox.x+entity.wpn.getComponent(MeleeWeaponComponent.class).hitboxOffset.x;
        float y=entity.wpn.getComponent(MeleeWeaponComponent.class).hitBox.y+entity.wpn.getComponent(MeleeWeaponComponent.class).hitboxOffset.y;
        if((p.x>pc.position.x&&p.x<pc.position.x+dc.dimension.x+x)||(p.x<pc.position.x&&p.x>pc.position.x-x)
                &&(p.y>pc.position.y&&p.y<pc.position.y+dc.dimension.y+y)||(p.y<pc.position.y&&p.y>pc.position.y-y)) return true;
        return false;
    }


}
