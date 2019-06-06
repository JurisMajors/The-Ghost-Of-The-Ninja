package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import group4.ECS.etc.EntityConst;

public class HealthComponent implements Component {

    public int health;
    public EntityConst.EntityState state;

    public HealthComponent (int health) {
        this.health = health;
        this.state = EntityConst.EntityState.DEFAULT;
    }

}
