package group4.ECS.components.events;

public class TickComponent extends TimedComponent {

    public int duration;

    /**
     * This component holds information about the time it takes for an timed entity to expire
     *
     * @param duration the duration (in ticks) it takes for this event to expire
     */
    public TickComponent(int duration) {
        this.duration = duration;
    }

}
