package group4.ECS.entities.totems;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class TotemHelp extends Entity {
    Vector3f dim = new Vector3f(1.5f,2,0);
    private static TotemHelp me = new TotemHelp(new Vector3f());

    TotemHelp(Vector3f pos) {
        this.add(new PositionComponent(pos));
        this.add(new DimensionComponent(dim));
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.TOTEM_HELP, dim, false));
    }

    public static TotemHelp getHelp(Vector3f position) {
        me = new TotemHelp(position);
        return me;
    }

    public static TotemHelp getInstance() {
        return me;
    }

}
