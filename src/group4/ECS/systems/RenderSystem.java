package group4.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.game.Main;
import group4.maths.Matrix4f;

import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengles.GLES20.glActiveTexture;

/**
 * This system renders all entities which are in the graphicsFamily, thus having a
 * position as well as a graphics component
 */
public class RenderSystem extends EntitySystem {

    // array of registered entities in the graphicsFamily
    private ImmutableArray<Entity> entities;

    public RenderSystem() {}

    /**
     * @param priority used for creating a systems pipeline (lowest prio gets executed first)
     */
    public RenderSystem(int priority) {}

    /**
     * when added to engine, this function will register all entities in the graphicsFamily
     * @param engine a single instance throughout program which manages ECS
     */
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Families.graphicsFamily);
    }

    /**
     * Do something when this system gets removed from engine
     * @param engine a single instance throughout program which manages ECS
     */
    public void removedFromEngine(Engine engine) {}

    /**
     * The heart piece of a system. This method gets called on each
     * update on the single instance of the engine
     * @param deltaTime
     */
    public void update(float deltaTime) {

        // for each registered entity, render it statically
        // TODO: pass on camera information
        for (Entity entity : entities) {
            PositionComponent pc = Mappers.positionMapper.get(entity);
            GraphicsComponent gc = Mappers.grapicsMapper.get(entity);

            gc.shader.bind();
            gc.shader.setUniformMat4f("pr_matrix", Matrix4f.orthographic(
                    0, Main.SCREEN_WIDTH, 0, Main.SCREEN_HEIGHT, -1.0f, 1.0f)
            );
            gc.shader.setUniformMat4f("vw_matrix", Matrix4f.translate(pc.position));
            gc.shader.setUniform1f("tex", 1);
            gc.texture.bind();
            glActiveTexture(GL_TEXTURE1);
            gc.triangle.render();
        }

    }

    /**
     * @return whether this system is active or not
     */
    public boolean checkProcessing() { return true; }

    /**
     * Set the system to be either active (true) or inactive (false)
     * @param processing in {true, false}
     */
    public void setProcessing(boolean processing) {}

}
