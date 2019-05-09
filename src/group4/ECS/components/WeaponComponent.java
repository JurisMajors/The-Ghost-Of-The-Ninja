package group4.ECS.components;

import com.badlogic.ashley.core.Component;

public class WeaponComponent implements Component {

    public int damage;

    public WeaponComponent(int damage) {
        this.damage = damage;
    }
}
