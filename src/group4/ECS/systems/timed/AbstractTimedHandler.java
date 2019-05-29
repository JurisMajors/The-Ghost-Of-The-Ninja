package group4.ECS.systems.timed;

import group4.ECS.components.physics.CollisionComponent;

import javax.swing.text.html.parser.Entity;

public abstract class AbstractTimedHandler<T extends Entity> {

    public abstract void doOnInit(T e, CollisionComponent cc);

    public abstract void doOnElapsed(T e, CollisionComponent cc);

}
