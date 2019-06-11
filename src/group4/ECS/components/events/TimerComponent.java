package group4.ECS.components.events;

import com.badlogic.ashley.core.Component;

public class TimerComponent implements Component {

    // subtract delta-time from this
    public float timer;

    public TimerComponent(float duration) {
        this.timer = duration;
    }

}
