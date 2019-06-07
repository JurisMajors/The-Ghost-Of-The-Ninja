package group4.ECS.components.events;

import com.badlogic.ashley.core.Entity;
import group4.ECS.etc.TheEngine;

public class Event extends Entity {

    private Function fct;
    private Entity subject;

    /**
     * create anonymous Events or subclass to override invoke functionality
     *
     * @param subject subject to event
     */
    public Event(Entity subject, int duration, Function fct) {
        this.subject = subject;
        this.fct = fct;
        System.out.println(fct);
        this.add(new EventComponent(duration));
        TheEngine.getInstance().addEntity(this);
    }

    /**
     * will get called
     */
    public void invoke() {
        System.out.println("herhe");
        fct.invoke(subject);
    };

}
