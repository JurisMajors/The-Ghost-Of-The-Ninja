package group4.ECS.entities.items.weapons;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.stats.RangeWeaponComponent;
import group4.ECS.etc.EntityConst;
import group4.maths.Vector3f;

public class MobRangedAttack extends Entity {

    public MobRangedAttack(int dmg, float cooldown, Vector3f hitBoxOffset, EntityConst.BulletType type) {

        this.add(new RangeWeaponComponent(dmg, cooldown, hitBoxOffset, type));

    }

}
