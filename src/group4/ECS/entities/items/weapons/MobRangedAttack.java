package group4.ECS.entities.items.weapons;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.stats.RangeWeaponComponent;
import group4.ECS.etc.EntityConst;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class MobRangedAttack extends Entity {

    public MobRangedAttack(int dmg, float cooldown, Vector3f hitBoxOffset, Vector3f bulletHitbox,
                           Texture texture, float accuracy) {
        this.add(new RangeWeaponComponent(dmg, cooldown, hitBoxOffset, bulletHitbox, texture, accuracy));
    }

}
