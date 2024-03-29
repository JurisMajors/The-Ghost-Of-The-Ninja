package group4.ECS.entities.totems;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.etc.Mappers;
import group4.graphics.RenderLayer;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public abstract class Totem extends Entity {

    private Vector3f dimension = new Vector3f(2f, 2f, 0);
    public static int HELPCOST = 100;
    public static int CARRYCOST = 500;
    public static int CHALLANGEREWARD = 500;

    protected String name; // name of the totem

    public Level level; // level taht it resides in

    //public Vector3f rgbMask;

    public Totem(Vector3f position, String name, Level level) {
        this.name = name;
        this.level = level;
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new CollisionComponent(null));
        this.add(getGraphicsComponent());
        setRbgMask();
    }

    private GraphicsComponent getGraphicsComponent() {
        Shader shader = Shader.TOTEM;
        if (isEnd()) {
            return new GraphicsComponent(shader, Texture.TOTEM, dimension, RenderLayer.TOTEM, false);
        } else {
            return new GraphicsComponent(shader, Texture.TOTEM, dimension, RenderLayer.TOTEM, false);
        }
    }
    // unique identification of the totem
    public int getID() {
        return Character.getNumericValue(this.name.charAt(1));
    }

    public abstract boolean isEnd();

    public static boolean isEnd(String name) {
        return name.charAt(0) == 'e';
    }

    private void setRbgMask() {
        GraphicsComponent gc = Mappers.graphicsMapper.get(this);
        Vector3f rgbMask;
        // use the ID to create a unique colour mask
        switch (getID()) {
            case 0:
                rgbMask = new Vector3f(0.1f, 0f, 0f);
                break;
            case 1:
                rgbMask = new Vector3f(0f, 0.25f, 0f);
                break;
            case 2:
                rgbMask = new Vector3f(0f, 0f, 0.25f);
                break;
            case 3:
                rgbMask = new Vector3f(0.25f, 0.25f, 0f);
                break;
            case 4:
                rgbMask = new Vector3f(0.25f, 0f, 0.25f);
                break;
            case 5:
                rgbMask = new Vector3f(0f, 0.25f, 0.25f);
                break;
            case 6:
                rgbMask = new Vector3f(1f, 0f, 0f);
                break;
            default:
                rgbMask = new Vector3f(1f, 1f, 1f);
                break;
        }
        // set the color mask
        gc.setColorMask(rgbMask);
    }

}
