package group4.ECS.components.stats;

import group4.graphics.Texture;
import group4.maths.Vector3f;

public class RangeWeaponComponent extends WeaponComponent {

    public Vector3f bulletOffset; // position where the bullets appear with respect to the entity position
    public Vector3f bulletHitbox; // basically its dimension
    public Texture texture;
    public float accuracy;

    /**
     * @param damage
     * @param cooldown
     * @param bulletOffset
     * @param texture
     * @param bulletHitbox
     * @param accuracy accuracy in [0,1], 1 is very accurate (i.e. no angle) and 0 is very inaccurate (i.e. +-90 degree)
     */
    public RangeWeaponComponent(int damage, float cooldown,
                                Vector3f bulletOffset, Vector3f bulletHitbox, Texture texture, float accuracy) {
        super(damage, cooldown);
        this.bulletOffset = bulletOffset;
        this.texture = texture;
        this.bulletHitbox = bulletHitbox;
        this.accuracy = accuracy;
    }

}
