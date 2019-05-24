package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class WeaponComponent implements Component {

    public int damage;
    public Vector3f gripPos;
    public int rateOfAttack;
    public float cooldown;

    public WeaponComponent(int damage, int rateOfAttack, Vector3f gripPos) {
        this.damage = damage;
        this.rateOfAttack = rateOfAttack;
        this.gripPos = gripPos;
        this.cooldown = 0.0f;
    }

}
