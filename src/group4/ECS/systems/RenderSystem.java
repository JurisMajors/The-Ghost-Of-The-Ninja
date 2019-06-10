package group4.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.CameraComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.BodyPart;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.entities.totems.Totem;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.graphics.RenderLayer;
import group4.graphics.Shader;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;
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
    private boolean DEBUG = false;
    // array of registered entities in the graphicsFamily
    private ImmutableArray<Entity> entities;

    public RenderSystem() {
    }

    /**
     * @param priority used for creating a systems pipeline (lowest prio gets executed first)
     */
    public RenderSystem(int priority) {
        super(priority);
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
        Map<RenderLayer, List<Entity>> entityLayers = sortEntitiesByLayer();

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
        MovementComponent mc;
        for (RenderLayer layer : RenderLayer.values()) {
            glClear(GL_DEPTH_BUFFER_BIT); // Allows drawing on top of all the other stuff
            for (Entity entity : entityLayers.get(layer)) {
                if (entity instanceof HierarchicalPlayer) {
                    // Get components via mapper for O(1) component retrieval
                    pc = Mappers.positionMapper.get(entity);
                    mc = Mappers.movementMapper.get(entity);
                    gc = Mappers.graphicsMapper.get(entity);
                    if (mc.orientation == MovementComponent.LEFT) {
                        // Set the mirrored projection matrix
                        gc.shader.setUniformMat4f("pr_matrix", cc.projectionMatrixHorizontalFlip);
                        // Since player aligns with center screen with its bottom left corner, we need to temporarily
                        // also offset the view matrix.
                        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
                        Vector3f currentTranslation = cc.viewMatrix.getTranslation();
                        gc.shader.setUniformMat4f("vw_matrix",
                                Matrix4f.translate(
                                        currentTranslation.sub(new Vector3f(dc.dimension.x, 0.0f, 0.0f)))
                        );
                    }
                    // Loop over the entities hierarchy and draw it correctly
                    for (BodyPart bp : ((HierarchicalPlayer) entity).hierarchy) {
                        gc = bp.getComponent(GraphicsComponent.class);

                        // Bind shader
                        gc.shader.bind();

                        // create color overlay over textures
                        handleColorMask(gc);

                        // Set uniforms
                        gc.shader.setUniformMat4f("md_matrix", bp.getModelMatrix()); // Tmp fix for giving correct positions to vertices in the vertexbuffers
                        gc.shader.setUniform1f("tex", gc.texture.getTextureID()); // Specify which texture slot to use

                        // Bind texture and specify texture slot
                        gc.texture.bind();
                        glActiveTexture(gc.texture.getTextureID());

                        // Render!
                        gc.geometry.render();
                    }

                    // Restore the default projection and view matrices
                    gc.shader.setUniformMat4f("pr_matrix", cc.projectionMatrix);
                    gc.shader.setUniformMat4f("vw_matrix", cc.viewMatrix);


                } else {
                    // Get components via mapper for O(1) component retrieval
                    pc = Mappers.positionMapper.get(entity);
                    gc = Mappers.graphicsMapper.get(entity);

                    // Bind shader
                    gc.shader.bind();

                    // create color overlay over textures
                    handleColorMask(gc);

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

            for (Entity e : entities) {
                // draw spline paths during debug
                if (Mappers.splinePathMapper.get(e) != null) {
                    DebugUtils.drawSpline(Mappers.splinePathMapper.get(e).points);
                }
                // draw the velocity of all mobs
                if (e instanceof Mob) {
                    pc = Mappers.positionMapper.get(e);
                    mc = Mappers.movementMapper.get(e);
                    DebugUtils.drawLine(pc.position, pc.position.add(mc.velocity));
                }
            }

            // Temporary example for drawing lines or boxes.
            // NOTE: Uncomment to see the effect
            for (Entity a : entities) { // For all A, for all B...  N^2 loop
                PositionComponent pca = Mappers.positionMapper.get(a);
                DimensionComponent dca = Mappers.dimensionMapper.get(a);
                DebugUtils.drawBox(pca.position, pca.position.add(dca.dimension));

//                for (int i = 0; i < entities.size(); i++) { // NOTE: Can't access Iterator in a nested fashion for some reason.. Hence the for(i = 0... style
////                    Entity b = entities.get(i);
////                    PositionComponent pcb = Mappers.positionMapper.get(b);
////                    DebugUtils.drawLine(pca.position, pcb.position);
////                    DebugUtils.drawCircle(pca.position, 2.0f, 50);
//                }

                if (a instanceof HierarchicalPlayer) {
                    DebugUtils.drawCircle(a.getComponent(PositionComponent.class).position.add(new Vector3f(a.getComponent(DimensionComponent.class).dimension.x / 2, 0.8f, 0.0f)), 0.9f, 50);
                }
            }

            DebugUtils.flush();
        }
    }

    /**
     * Adds a color on top of the texture of a graphics component.
     * If the graphics component has a personal color that takes priority.
     * Else we look at the global color mask in GraphicsComponent.GLOBAL_COLOR_MASK.
     * If neither are set we have a color mask of 0 which does nothing.
     * @param gc graphics component
     */
    private void handleColorMask(GraphicsComponent gc) {
        if (gc.hasMask) { // per texture mask has priority
            gc.shader.setUniform3f("color_mask", gc.colorMask);
        } else if (GraphicsComponent.HAS_MASK) { // global mask comes next
            gc.shader.setUniform3f("color_mask", GraphicsComponent.GLOBAL_COLOR_MASK);
        } else { // no mask
            gc.shader.setUniform3f("color_mask", new Vector3f());
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
    private Map<RenderLayer, List<Entity>> sortEntitiesByLayer() {
        // Construct an empty hashmap and create a key for each layer
        Map<RenderLayer, List<Entity>> entityLayers = new HashMap<>();
        for (RenderLayer layer : RenderLayer.values()) {
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
