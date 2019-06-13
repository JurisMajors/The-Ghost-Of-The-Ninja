package group4.ECS.components.stats;

import group4.graphics.Texture;
import group4.maths.Vector3f;

public class RangeWeaponComponent extends WeaponComponent {

    public Vector3f bulletOffset; // position where the bullets appear with respect to the entity position
    public Vector3f bulletHitbox; // basically its dimension
    public Texture texture;

    /**
     * @param damage
     * @param cooldown
     * @param bulletOffset
     * @param texture
     * @param bulletHitbox
     */
    public RangeWeaponComponent(int damage, float cooldown,
                                Vector3f bulletOffset, Vector3f bulletHitbox, Texture texture) {
        super(damage, cooldown);
        this.bulletOffset = bulletOffset;
        this.texture = texture;
        this.bulletHitbox = bulletHitbox;
    }

}
