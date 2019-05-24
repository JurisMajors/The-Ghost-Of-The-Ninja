package group4.ECS.components.stats;

import group4.ECS.etc.EntityConst;
import group4.maths.Vector3f;

public class RangeWeaponComponent extends WeaponComponent {

    public Vector3f bulletPos; //position where the bullets appear with respect to the entity position
    public EntityConst.BulletType type;

    public RangeWeaponComponent(int damage, int rateOfDamage,
                                Vector3f bulletPos, EntityConst.BulletType type) {
        super(damage, rateOfDamage);
        this.bulletPos = bulletPos;
        this.type = type;
    }

}
