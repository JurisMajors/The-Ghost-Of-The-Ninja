package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class WeaponComponent implements Component {

    public int damage;
    public float rateOfAttack;
    public float cooldown;

    public WeaponComponent(int damage, float rateOfAttack) {
        this.damage = damage;
        this.rateOfAttack = rateOfAttack;
        this.cooldown = 0.0f;
    }

}
