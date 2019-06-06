package group4.ECS.entities.damage;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.events.TimerComponent;
import group4.maths.Vector3f;

import java.util.Set;

public class DamageAreaTemp extends DamageArea {

    /**
     *
     * @param position position of the damage area
     * @param dimension dimension field of effect
     * @param damage damage inflicted on colliding entity (HealthComponent needed)
     */
    public DamageAreaTemp(Vector3f position, Vector3f dimension, int damage,
                          float duration, Set<Class<? extends Entity>> excluded) {
        super(position, dimension, damage, excluded);
        this.add(new TimerComponent(duration));
    }

}
