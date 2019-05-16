package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PositionComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class Exit extends Entity {

    Module module;

    /**
     * Construct a simple block (flexible texture and shader) in a certain position of certain size
     *
     * @param p       The position of the block (lower left corner)
     * @param d       The dimensions of the block
     */
    public Exit(Vector3f p, Vector3f d, Module m) {
        this.add(new PositionComponent(p));
        this.add(new DimensionComponent(d));
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.EXIT, d));
        this.module = m;
    }


}
