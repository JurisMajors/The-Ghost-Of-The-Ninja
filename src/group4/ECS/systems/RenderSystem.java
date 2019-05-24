package group4.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.CameraComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.BodyPart;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.entities.Player;
import group4.ECS.entities.mobs.FlappingMob;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.graphics.RenderLayer.Layer;
import group4.graphics.Shader;
import group4.maths.Matrix4f;
import group4.utils.DebugUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL41.*;

/**
 * This system renders all entities which are in the graphicsFamily, thus having a
 * position as well as a graphics component
 */
public class RenderSystem extends EntitySystem {
    private boolean DEBUG = true;
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
        Map<Layer, List<Entity>> entityLayers = sortEntitiesByLayer();

        // Get the camera and its main component from the engine
        Entity camera = TheEngine.getInstance().getEntitiesFor(Families.cameraFamily).get(0); // There should only be one camera currently
        CameraComponent cc = camera.getComponent(CameraComponent.class);

        // Update the projection and view matrices for all shaders once
        for (Shader shader : Shader.getAllShaders()) {
            shader.bind();
            shader.setUniformMat4f("pr_matrix", cc.projectionMatrix);
            shader.setUniformMat4f("vw_matrix", cc.viewMatrix);
        }

        PositionComponent pc;
        GraphicsComponent gc;
        for (Layer layer : Layer.values()) {
            glClear(GL_DEPTH_BUFFER_BIT); // Allows drawing on top of all the other stuff
            for (Entity entity : entityLayers.get(layer)) {
                if (entity instanceof HierarchicalPlayer) {
                    // Get components via mapper for O(1) component retrieval
                    pc = Mappers.positionMapper.get(entity);

                    // Loop over the entities hierarchy and draw it correctly
                    for (BodyPart bp : ((HierarchicalPlayer) entity).hierarchy) {
                        gc = bp.getComponent(GraphicsComponent.class);

                        // Bind shader
                        gc.shader.bind();

                        // Set uniforms
                        gc.shader.setUniformMat4f("md_matrix", Matrix4f.translate(pc.position.add(bp.relativePosition))); // Tmp fix for giving correct positions to vertices in the vertexbuffers
                        gc.shader.setUniform1f("tex", gc.texture.getTextureID()); // Specify which texture slot to use

                        // Bind texture and specify texture slot
                        gc.texture.bind();
                        glActiveTexture(gc.texture.getTextureID());

                        // Render!
                        gc.geometry.render();
                    }

                } else {
                    // Get components via mapper for O(1) component retrieval
                    pc = Mappers.positionMapper.get(entity);
                    gc = Mappers.graphicsMapper.get(entity);

                    // Bind shader
                    gc.shader.bind();

                    // Set uniforms
                    gc.shader.setUniformMat4f("md_matrix", Matrix4f.translate(pc.position)); // Tmp fix for giving correct positions to vertices in the vertexbuffers
                    gc.shader.setUniform1f("tex", gc.texture.getTextureID()); // Specify which texture slot to use

                    // Bind texture and specify texture slot
                    gc.texture.bind();
                    glActiveTexture(gc.texture.getTextureID());

                    // Render!
                    gc.geometry.render();
                }
            }
        }

        // Start of debug drawing
        if (DEBUG) {
            glClear(GL_DEPTH_BUFFER_BIT); // Allows drawing on top of all the other stuff
            Shader.DEBUG.bind();
//            DebugUtils.drawGrid(1.0f);

            for (Entity e: entities) {
                // draw spline paths during debug
                if (Mappers.splinePathMapper.get(e) != null) {
                    DebugUtils.drawSpline(Mappers.splinePathMapper.get(e).points);
                }
                // draw the velocity of all mobs
                if (e instanceof Mob) {
                    pc = Mappers.positionMapper.get(e);
                    MovementComponent mc = Mappers.movementMapper.get(e);
                    DebugUtils.drawLine(pc.position, pc.position.add(mc.velocity));
                }
            }

            // Temporary example for drawing lines or boxes.
            // NOTE: Uncomment to see the effect
//            for (Entity a: entities) { // For all A, for all B...  N^2 loop
//                PositionComponent pca = Mappers.positionMapper.get(a);
//                for (int i = 0; i < entities.size(); i++) { // NOTE: Can't access Iterator in a nested fashion for some reason.. Hence the for(i = 0... style
//                    Entity b = entities.get(i);
//                    PositionComponent pcb = Mappers.positionMapper.get(b);
//                    DebugUtils.drawLine(pca.position, pcb.position);
//                    DebugUtils.drawBox(pca.position, pcb.position);
//                    DebugUtils.drawCircle(pca.position, 2.0f, 50);
//                }
//            }

            DebugUtils.flush();
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

    /**
     * Constructs a Map which stores all entities in the engine sorted to the layer
     * indicated in their respective GraphicsComponent.
     *
     * @return Map<Layer, List < Entity>>, the entities sorted by layer
     */
    private Map<Layer, List<Entity>> sortEntitiesByLayer() {
        // Construct an empty hashmap and create a key for each layer
        Map<Layer, List<Entity>> entityLayers = new HashMap<>();
        for (Layer layer : Layer.values()) {
            entityLayers.put(layer, new ArrayList<>());
        }

        GraphicsComponent gc;
        for (Entity entity : entities) {
            gc = Mappers.graphicsMapper.get(entity); // Fetch the GraphicsComponent
            entityLayers.get(gc.layer).add(entity); // Use layer as key for adding to the map
        }
        return entityLayers;
    }

}
