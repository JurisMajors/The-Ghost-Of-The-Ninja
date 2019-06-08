package group4.ECS.components.events;

import com.badlogic.ashley.core.Component;

public class EventComponent implements Component {

    public int duration;
    public int passed;

    /**
     * This component holds information about the time it takes for an event entity to expire
     *
     * @param duration the duration (in ticks) it takes for this event to expire
     */
    public EventComponent(int duration) {
        this.duration = duration;
        // time passed from invoking this Event
        this.passed = 0;
    }

}
