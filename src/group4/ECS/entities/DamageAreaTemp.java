package group4.ECS.entities;

import group4.ECS.components.identities.TimerComponent;
import group4.ECS.systems.timed.timedEventHandlers.AbstractTimedHandler;
import group4.maths.Vector3f;

public class DamageAreaTemp extends DamageArea {

    /**
     *
     * @param position position of the damage area
     * @param dimension dimension field of effect
     * @param damage damage inflicted on colliding entity (HealthComponent needed)
     */
    public DamageAreaTemp(Vector3f position, Vector3f dimension, int damage,
                          float duration, AbstractTimedHandler handler) {
        super(position, dimension, damage);
        this.add(new TimerComponent(duration, handler));
    }

}
