package group4.ECS.components.events;

import group4.ECS.systems.timed.AbstractTimedHandler;

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
