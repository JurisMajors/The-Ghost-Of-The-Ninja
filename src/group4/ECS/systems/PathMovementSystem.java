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

    public PathMovementSystem(int priority) {
        super(Families.pathFamily, priority);
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
        }
    }
}
