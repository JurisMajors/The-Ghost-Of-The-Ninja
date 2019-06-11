package group4.ECS.systems.GraphHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.AStarMobs.JumpingAStarMob;
import group4.ECS.entities.mobs.JumpingMob;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;
import group4.maths.Vector3f;

import java.util.ArrayList;

public class JumpingAStarMobGraphHandler extends AbstractGraphHandler<JumpingAStarMob> {
    private static AbstractGraphHandler agh = new JumpingWalkingAStarMobGraphHandler();

    @Override
    protected void generateFirstLayerNodes(Entity entity, ArrayList<Node> prevLayer, ArrayList<Node> currLayer) {
        super.generateFirstLayerNodes(entity, prevLayer, currLayer);
        for (int i = 0; i < prevLayer.size(); i++) {
            leftUpNode(entity, currLayer, prevLayer.get(i));
            rightUpNode(entity, currLayer, prevLayer.get(i));
            upNode(entity, currLayer, prevLayer.get(i));
        }
    }

    public static AbstractGraphHandler getInstance() {
        return agh;
    }
}
