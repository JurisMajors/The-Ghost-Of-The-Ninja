package group4.ECS.components;

import com.badlogic.ashley.core.Component;

public class StatsComponent implements Component {

    public int health = 0;
    public int attack = 0;
    public int speed = 0;
    // TODO: Weapon, Inventory

    /**
     * Constructs a stats component with initial values
     * @param h health value
     * @param a attack value
     * @param s speed value
     */
    public StatsComponent(int h, int a, int s) {
        this.health = h;
        this.attack = a;
        this.speed = s;
    }
}
