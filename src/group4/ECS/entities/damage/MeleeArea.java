package group4.ECS.entities.damage;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.events.Event;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

import java.util.Set;

public class MeleeArea extends DamageArea {

    public Event event;

    /**
     * This is a damage area specifically for melee, which takes the origin of damage
     * to prevent self hitting
     *
     * @param position  position of the damage area
     * @param dimension dimension field of effect
     * @param damage    damage inflicted on colliding entity (HealthComponent needed)
     * @param excluded  exclude for damage
     */
    public MeleeArea(Vector3f position, Vector3f dimension, int damage, Set<Class<? extends Entity>> excluded,
                     int duration) {
        super(position, dimension, damage, excluded);

         event = new Event(this, duration,
                 entity -> TheEngine.getInstance().removeEntity(entity));

    }

}
