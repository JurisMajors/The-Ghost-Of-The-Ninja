package group4.ECS.components;

import com.badlogic.ashley.core.Component;
import group4.ECS.etc.EntityConst;

public class WeaponComponent implements Component {

    public int damage;
    public int fireRate;
    public EntityConst.BulletType type;

    public WeaponComponent(int damage, int fireRate, EntityConst.BulletType type) {
        this.damage = damage;
        this.fireRate = fireRate;
        this.type = type;
    }

}
