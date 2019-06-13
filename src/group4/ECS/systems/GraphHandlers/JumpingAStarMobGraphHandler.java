package group4.ECS.systems.GraphHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.entities.AStarMobs.JumpingAStarMob;

import java.util.ArrayList;

public class JumpingAStarMobGraphHandler extends AbstractGraphHandler<JumpingAStarMob> {
    private static AbstractGraphHandler agh = new JumpingAStarMobGraphHandler();

    @Override
    protected void generateFirstLayerNodes(Entity entity, ArrayList<Node> prevLayer, ArrayList<Node> currLayer) {
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
