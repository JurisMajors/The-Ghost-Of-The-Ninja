package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.ECS.etc.EntityConst;

public class WeaponComponent implements Component {

    public int fireRate;
    public EntityConst.BulletType type;

    public WeaponComponent(int fireRate, EntityConst.BulletType type) {
        this.fireRate = fireRate;
        this.type = type;
    }

}
