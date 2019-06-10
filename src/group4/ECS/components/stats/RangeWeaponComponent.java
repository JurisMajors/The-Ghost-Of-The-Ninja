package group4.ECS.components.stats;

import group4.ECS.etc.EntityConst;
import group4.maths.Vector3f;

public class RangeWeaponComponent extends WeaponComponent {

    public Vector3f bulletPos; //position where the bullets appear with respect to the entity position
    public EntityConst.BulletType type;

    /**
     * @param damage
     * @param cooldown
     * @param bulletPos
     * @param type
     */
    public RangeWeaponComponent(int damage, int cooldown,
                                Vector3f bulletPos, EntityConst.BulletType type) {
        super(damage, cooldown);
        this.bulletPos = bulletPos;
        this.type = type;
    }

}
