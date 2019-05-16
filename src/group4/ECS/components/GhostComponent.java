package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.AI.Brain;

public class GhostComponent implements Component {

    public Brain brain;
    public int[] moveFreq;

    public GhostComponent(Brain brain) {
        this.brain = brain;
        moveFreq = new int[] {0, 0, 0};
    }
}
