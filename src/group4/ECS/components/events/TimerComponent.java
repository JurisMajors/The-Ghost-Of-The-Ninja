package group4.ECS.components.events;

import com.badlogic.ashley.core.Component;
import group4.ECS.systems.timed.timedEventHandlers.AbstractTimedHandler;

public class TimerComponent extends TimedComponent {

    // handler for timed event
    public AbstractTimedHandler handler;

    // subtract delta-time from this
    public float timer;

    public TimerComponent(float duration, AbstractTimedHandler handler) {
        this.timer = duration;
        this.handler = handler;
    }

}
