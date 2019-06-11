package group4.ECS.systems.GraphHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.AStarMobs.WalkingAStarMob;
import group4.ECS.entities.mobs.WalkingMob;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;
import group4.maths.Vector3f;

import java.util.ArrayList;

public class WalkingAStarMobGraphHandler extends AbstractGraphHandler<WalkingAStarMob> {
    private static AbstractGraphHandler agh = new WalkingAStarMobGraphHandler();

    @Override
    protected void generateFirstLayerNodes(Entity entity, ArrayList<Node> prevLayer, ArrayList<Node> currLayer) {
        for (int i = 0; i < prevLayer.size(); i++) {
            leftNode(entity, currLayer, prevLayer.get(i));
            rightNode(entity, currLayer, prevLayer.get(i));
        }
    }

    @Override
    protected void posYHValues(Entity entity) {
        GraphComponent gc = Mappers.graphMapper.get(entity);
        gc.yHValue.put((float) gc.module.getHeight(), 1000000);
    }

    public static AbstractGraphHandler getInstance() {
        return agh;
    }
}
