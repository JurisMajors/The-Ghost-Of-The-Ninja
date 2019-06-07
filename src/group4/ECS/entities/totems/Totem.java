package group4.ECS.entities.totems;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.systems.collision.CollisionHandlers.TotemCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class Totem extends Entity {

    private Vector3f dimension = new Vector3f(1.0f, 2f, 0);

    private String name;

    public Level level;

    public Vector3f rgbMask;

    public Totem(Vector3f position, String name, Level level) {
        this.name = name;
        this.level = level;
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new CollisionComponent(TotemCollision.getInstance()));
        this.add(getGraphicsComponent());
        setRbgMask();
    }

    private GraphicsComponent getGraphicsComponent() {
        Shader shader = Shader.SIMPLE;
        // JORIS TODO: correct shader!!
//        shader = Shader.TOTEM;
        if (isEnd()) {
            return new GraphicsComponent(shader, Texture.TOTEM_END, dimension, false);
        } else {
            return new GraphicsComponent(shader, Texture.TOTEM_START, dimension, false);
        }
    }

    public int getID() {
        return Integer.parseInt(this.name.substring(1));
    }

    public boolean isEnd() {
        return this.name.charAt(0) == 'e';
    }

    public static String getStartName() {
        return "totemStart";
    }

    public static String getEndName() {
        return "totemEnd";
    }

    private void setRbgMask() {
        // use the ID to create a unique colour mask
        rgbMask = new Vector3f(0f, 1f, 0.25f);
    }

    public Vector3f getRbgMask() {
        return rgbMask;
    }
}