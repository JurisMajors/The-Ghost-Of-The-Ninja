package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;
import group4.AI.Brain;
import group4.AI.GhostMove;

public class GhostComponent implements Component {

    public Brain brain;
    public int[] moveFreq;

    public GhostComponent(Brain brain) {
        this.brain = brain;
        moveFreq = new int[GhostMove.moveAmount()];
    }
}
