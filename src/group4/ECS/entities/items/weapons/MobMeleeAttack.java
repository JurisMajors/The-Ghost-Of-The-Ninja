package group4.ECS.entities.items.weapons;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.maths.Vector3f;

public class MobMeleeAttack extends Entity {

    public MobMeleeAttack(int dmg, float cooldown, Vector3f hitBox, Vector3f hitBoxOffset) {

        this.add(new MeleeWeaponComponent(dmg, cooldown, hitBox, hitBoxOffset));

    }

}
