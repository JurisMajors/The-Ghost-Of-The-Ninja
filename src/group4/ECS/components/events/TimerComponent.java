package group4.ECS.components.events;

public class TimerComponent extends TimedComponent {

    // subtract delta-time from this
    public float timer;

    public TimerComponent(float duration) {
        this.timer = duration;
    }

}
