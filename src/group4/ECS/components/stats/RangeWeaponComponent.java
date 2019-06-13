package group4.ECS.components.stats;

import group4.ECS.etc.EntityConst;
import group4.maths.Vector3f;

public class RangeWeaponComponent extends WeaponComponent {

    public Vector3f bulletOffset; //position where the bullets appear with respect to the entity position
    public EntityConst.BulletType type;

    /**
     * @param damage
     * @param cooldown
     * @param bulletOffset
     * @param type
     */
    public RangeWeaponComponent(int damage, float cooldown,
                                Vector3f bulletOffset, EntityConst.BulletType type) {
        super(damage, cooldown);
        this.bulletOffset = bulletOffset;
        this.type = type;
    }

}
