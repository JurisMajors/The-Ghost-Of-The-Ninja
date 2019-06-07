package group4.ECS.entities;

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

    private Vector3f dimension = new Vector3f(1.0f, 2f,0);

    private String name;

    private Ghost ghost;

    public Level level;

    Totem(Vector3f position, String name, String ghostDir, Level level) {
        this.name = name;
        this.level = level;
        this.setGhost(ghostDir);
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new CollisionComponent(TotemCollision.getInstance()));
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.DEBUG, dimension, false));
    }

    private void setGhost(String dir) {
        this.ghost = new Ghost(this.getComponent(PositionComponent.class).position,
                this.level, dir + getID());
    }

    public Ghost getGhost() {
        return this.ghost;
    }

    public boolean isEnd() {
        return this.name.charAt(0) == 'e';
    }

    public int getID() {
        return Integer.parseInt(this.name.substring(1));
    }
}
