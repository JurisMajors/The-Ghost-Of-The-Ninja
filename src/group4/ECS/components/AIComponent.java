package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.AI.Brain;

public class AIComponent implements Component {

    public Brain brain;

    public AIComponent(Brain brain) {
        this.brain = brain;
    }
}
