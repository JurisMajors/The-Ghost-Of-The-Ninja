package group4.ECS.components.stats;

import group4.maths.Vector3f;

public class MeleeWeaponComponent extends WeaponComponent {

    public Vector3f hitBox;
    public Vector3f hitboxOffset;

    /**
     * This Component is a subclass to {@link WeaponComponent} which should be included in every
     * melee weapon
     *
     * @param damage
     * @param hitBox
     * @param cooldown
     * @param hitboxOffset
     */
    public MeleeWeaponComponent(int damage, float cooldown,
                                Vector3f hitBox, Vector3f hitboxOffset) {
        super(damage, cooldown);
        this.hitBox = hitBox;
        this.hitboxOffset = hitboxOffset;
    }

}
