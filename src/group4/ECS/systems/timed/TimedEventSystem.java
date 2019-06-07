package group4.ECS.systems.timed;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.events.Event;
import group4.ECS.components.events.EventComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;

public class TimedEventSystem extends IteratingSystem {

    /**
     * This System handles timed events
     *
     * @param priority
     */
    public TimedEventSystem(int priority) {
        super(Families.timedEventFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EventComponent ec;
        System.out.println(entity);

        // if entity expires on ticks
        if (Mappers.eventMapper.get(entity) != null) {
            ec = Mappers.eventMapper.get(entity);

            // if expired, remove from the engine
            if (ec.duration == ec.passed) {
                TheEngine.getInstance().removeEntity(entity);
            } else {    // else tick--
                System.out.println("111111111111");
                ((Event) entity).invoke();
                ec.passed += 1;
            }

        }
    }

}
