package group4.ECS.components.stats;

import group4.maths.Vector3f;

public class MeleeWeaponComponent extends WeaponComponent {

    public Vector3f hitBox;
    public Vector3f hitboxOffset;

    public MeleeWeaponComponent(int damage, float rateOfDamage,
                                Vector3f hitBox, Vector3f hitboxOffset) {
        super(damage, rateOfDamage);
        this.hitBox = hitBox;
        this.hitboxOffset = hitboxOffset;
    }

}
