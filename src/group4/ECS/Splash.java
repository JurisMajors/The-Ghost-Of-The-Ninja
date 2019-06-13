package group4.ECS;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.events.Event;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.etc.TheEngine;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class Splash extends Entity {

    public Splash(Vector3f position, int duration) {
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.SPLASH, new Vector3f(1f, 1f, 0f), false));
        this.add(new PositionComponent(position));

        TheEngine.getInstance().addEntity(this);
        new Event(this, duration, ((entity, dur, passed) ->
        {
            if (passed == dur) TheEngine.getInstance().removeEntity(entity);
        }));
    }
}
