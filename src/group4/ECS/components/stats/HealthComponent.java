package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {

    public int health;

    public HealthComponent(int health) {
        this.health = health;
    }
}
