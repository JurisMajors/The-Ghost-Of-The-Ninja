package group4.ECS.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.GraphComponent;
import group4.ECS.components.PathComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;
import group4.utils.DebugUtils;

public class PathMovementSystem extends IteratingSystem {
    int vertexID = 0;

    public PathMovementSystem() {
        super(Families.pathFamily);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        GraphComponent gc = Mappers.graphMapper.get(entity);
        PositionComponent pc = Mappers.positionMapper.get(entity);
        PathComponent pathc = Mappers.pathMapper.get(entity);

        if (pathc.coordinates.size() > 0) {
            gc.vertexID = pathc.vertexID.getFirst();
            pathc.vertexID.removeFirst();
            pc.position = pathc.coordinates.getFirst();
            pathc.coordinates.removeFirst();
            /*for (int i = pathc.index + 1; i < pathc.direction.length; i++)
                DebugUtils.drawLine(pathc.direction[i - 1], pathc.direction[i]);*/
        }
        if (gc.vertexID != -1) vertexID = gc.vertexID;
        for (int i = 0; i < gc.neighbours.get(vertexID).size(); i++) {
            for (int j = 1; j < gc.edgePath.get(gc.edgeID[vertexID][gc.neighbours.get(vertexID).get(i)]).size(); j++) {
                DebugUtils.drawLine(gc.edgePath.get(gc.edgeID[vertexID][gc.neighbours.get(vertexID).get(i)]).get(j - 1), gc.edgePath.get(gc.edgeID[vertexID][gc.neighbours.get(vertexID).get(i)]).get(j));
            }
        }

        /*GraphComponent mgc = Mappers.moduleGraphMapper.get(entity);
        for(int i=0;i<mgc.xCoords.length;i++)if(mgc.collisionPairs.containsKey(mgc.xCoords[i])){
            ArrayList<Float[]> list=mgc.collisionPairs.get(mgc.xCoords[i]);
            for(int j=0;j<list.size();j++){
                DebugUtils.drawLine(new Vector3f(mgc.xCoords[i],list.get(j)[0],0),
                        new Vector3f(mgc.xCoords[i],list.get(j)[1]+1,0));
            }
        }


        GraphComponent mgc = Mappers.moduleGraphMapper.get(entity);
        for(int i=0;i<mgc.vertexCoords.length;i++){
                DebugUtils.drawLine(mgc.vertexCoords[i],mgc.vertexCoords[i].add(new Vector3f(0,1,0)));
        }*/
        /*int q=0;
        GraphComponent mgc = Mappers.moduleGraphMapper.get(entity);
        if(mgc.coordsVertex.containsKey(pc.position.x)&&
                mgc.coordsVertex.get(pc.position.x).containsKey(pc.position.y)) {
            q = mgc.coordsVertex.get(pc.position.x).get(pc.position.y);
            //System.out.println(q);
        }
        for(int i=0;i<mgc.neighbours[q].length;i++){
            for(int j=1;j<mgc.edgePath[mgc.edgeID[q][mgc.neighbours[q][i]]].length;j++){
                DebugUtils.drawLine(mgc.edgePath[mgc.edgeID[q][mgc.neighbours[q][i]]][j-1],mgc.edgePath[mgc.edgeID[q][mgc.neighbours[q][i]]][j]);
            }
        }*/
        /*q=(q+1)%mgc.vertexCoords.length;*/

    }
}
