package group4.ECS.systems.timed;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.events.TickComponent;
import group4.ECS.components.events.TimedComponent;
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
        TimedComponent tc;

        // if entity expires on ticks
        if (Mappers.tickMapper.get(entity) != null) {
            tc = Mappers.tickMapper.get(entity);

            // if expired, remove from the engine
            if (((TickComponent) tc).duration <= 0) {
                TheEngine.getInstance().removeEntity(entity);
            } else {    // else tick--
                ((TickComponent) tc).duration -= 1;
            }
        } else if (Mappers.timedMapper.get(entity) != null) {   // else if we are dealing with real time
            tc = Mappers.timedMapper.get(entity);

            // TODO: timed
        } else {
            throw new IllegalStateException("entity must have either timed or tick component");
        }
    }

}
