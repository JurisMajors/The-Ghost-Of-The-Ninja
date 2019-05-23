package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class WeaponComponent implements Component {

    public int damage;
    public Vector3f gripPos;

    public WeaponComponent(int damage, Vector3f gripPos) {
        this.damage = damage;
        this.gripPos = gripPos;
    }

}
