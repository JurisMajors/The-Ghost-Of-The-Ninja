package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.events.TickComponent;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;

public class MeleeArea extends DamageArea {

    /**
     * This is a damage area specifically for melee, which takes the origin of damage
     * to prevent self hitting
     *
     * @param position  position of the damage area
     * @param dimension dimension field of effect
     * @param damage    damage inflicted on colliding entity (HealthComponent needed)
     * @param origin    origin of the damage
     */
    public MeleeArea(Vector3f position, Vector3f dimension, int damage, Entity origin) {
        super(position, dimension, damage);
        this.add(new TickComponent(1));
        Mappers.damageMapper.get(this).origin = origin;
    }

}
