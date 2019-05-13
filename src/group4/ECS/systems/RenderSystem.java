package group4.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.CameraComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.graphics.RenderLayer.Layer;
import group4.maths.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL41.glActiveTexture;

/**
 * This system renders all entities which are in the graphicsFamily, thus having a
 * position as well as a graphics component
 */
public class RenderSystem extends EntitySystem {

    // array of registered entities in the graphicsFamily
    private ImmutableArray<Entity> entities;

    public RenderSystem() {
    }

    /**
     * @param priority used for creating a systems pipeline (lowest prio gets executed first)
     */
    public RenderSystem(int priority) {
    }

    /**
     * when added to engine, this function will register all entities in the graphicsFamily
     *
     * @param engine a single instance throughout program which manages ECS
     */
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Families.graphicsFamily);
    }

    /**
     * Do something when this system gets removed from engine
     *
     * @param engine a single instance throughout program which manages ECS
     */
    public void removedFromEngine(Engine engine) {
    }

    /**
     * The heart piece of a system. This method gets called on each
     * update on the single instance of the engine
     *
     * @param deltaTime time between last and current update
     */
    public void update(float deltaTime) {
        // Match objects to RenderLayers so we can draw layer by layer
        // TODO: Maybe it's better to use a mapper for this?
        Map<Layer, List<Entity>> entityLayers = new HashMap<>();
        for (Layer layer : Layer.values()) {
            entityLayers.put(layer, new ArrayList<>());
        }

        GraphicsComponent gc;
        for (Entity entity : entities) {
            gc = Mappers.graphicsMapper.get(entity);
            entityLayers.get(gc.layer).add(entity);
        }

        // Get the camera and its main component from the engine
        Entity camera = TheEngine.getInstance().getEntitiesFor(Families.cameraFamily).get(0); // There should only be one camera currently
        CameraComponent cc = camera.getComponent(CameraComponent.class);

        for (Layer layer : Layer.values()) {
            System.out.println("Rendering " + layer);
            for (Entity entity : entityLayers.get(layer)) {
                // get mapper for O(1) component retrieval
                PositionComponent pc = Mappers.positionMapper.get(entity);
                gc = Mappers.graphicsMapper.get(entity);

                // Bind shader
                gc.shader.bind();

                // Set uniforms
                gc.shader.setUniformMat4f("pr_matrix", cc.projectionMatrix);
                gc.shader.setUniformMat4f("md_matrix", Matrix4f.translate(pc.position)); // Tmp fix for giving correct positions to vertices in the vertexbuffers
                gc.shader.setUniformMat4f("vw_matrix", cc.viewMatrix);
                gc.shader.setUniform1f("tex", gc.texture.getTextureID()); // Specify which texture slot to use

                // Bind texture and specify texture slot
                gc.texture.bind();
                glActiveTexture(gc.texture.getTextureID());

                // Render!
                gc.triangle.render(); // TODO: Triangle is an arbitrary (and probably bad name) which I think still remains from the first example we had hehe.. => Change :-)

            }
        }
    }

    /**
     * @return whether this system is active or not
     */
    public boolean checkProcessing() {
        return true;
    }

    /**
     * Set the system to be either active (true) or inactive (false)
     *
     * @param processing in {true, false}
     */
    public void setProcessing(boolean processing) {
    }

}
