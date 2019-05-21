package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.ECS.systems.collision.CollisionHandlers.ExitCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class Exit extends Entity {

    public Module module;

    /**
     * Construct a simple block (flexible texture and shader) in a certain position of certain size
     *
     * @param p       The position of the block (lower left corner)
     * @param d       The dimensions of the block
     * @param m       The module the exit is part of
     */
    public Exit(Vector3f p, Vector3f d, Module m) {
        this.add(new PositionComponent(p));
        this.add(new DimensionComponent(d));
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.EXIT, d));
        // TODO: one of these should be redundant and removed
        this.add(new CollisionComponent(ExitCollision.getInstance()));

        // Set the module this exit belongs to
        this.module = m;
    }


}
