package group4.ECS.systems.event;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.events.Event;
import group4.ECS.components.events.EventComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;

public class EventSystem extends IteratingSystem {

    /**
     * This System handles events
     *
     * @param priority
     */
    public EventSystem(int priority) {
        super(Families.timedEventFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EventComponent ec;

        // if entity expires on ticks
        if (Mappers.eventMapper.get(entity) != null) {
            ec = Mappers.eventMapper.get(entity);

            // if expired, remove from the engine
            System.out.println(entity);
            System.out.println(ec.duration + " " + ec.passed);
            if (ec.duration <= ec.passed) {
                ((Event) entity).invoke();
                TheEngine.getInstance().removeEntity(entity);
            } else {    // else tick--
                ((Event) entity).invoke();
                ec.passed += 1;
            }

        }
    }

}
