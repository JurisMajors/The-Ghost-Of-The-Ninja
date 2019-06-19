package group4.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.GraphComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.CameraComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.components.stats.ScoreComponent;
import group4.ECS.entities.BodyPart;
import group4.ECS.entities.Camera;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.entities.damage.DamageArea;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.entities.totems.Totem;
import group4.ECS.entities.world.ArtTile;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.game.Main;
import group4.graphics.RenderLayer;
import group4.graphics.Shader;
import group4.graphics.Text;
import group4.graphics.Texture;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;
import group4.utils.DebugUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL41.*;

/**
 * This system renders all entities which are in the graphicsFamily, thus having a
 * position as well as a graphics component
 */
public class RenderSystem extends EntitySystem {
    private boolean DEBUG = false;
    // array of registered entities in the graphicsFamily
    private ImmutableArray<Entity> entities;
    private Text textGenerator;
    private GraphicsComponent vignette = null;
    private GraphicsComponent noise = null;
    private GraphicsComponent loadingScreen = null;

    public RenderSystem() {
        this.textGenerator = new Text("src/group4/res/fonts/PressStart2P.ttf");
    }

    /**
     * @param priority used for creating a systems pipeline (lowest prio gets executed first)
     */
    public RenderSystem(int priority) {
        super(priority);
        this.textGenerator = new Text("src/group4/res/fonts/PressStart2P.ttf");
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
        glClearColor(
                21 / 255.0f,
                21 / 255.0f,
                29 / 255.0f,
                1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

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
        MovementComponent mc = null;
        ScoreComponent sc = null; // Must be initialized to at least null
        for (RenderLayer layer : RenderLayer.values()) {
            glClear(GL_DEPTH_BUFFER_BIT); // Allows drawing on top of all the other stuff
            for (Entity entity : entityLayers.get(layer)) {
                if (entity instanceof DamageArea) {
                    gc = Mappers.graphicsMapper.get(entity);
                    pc = Mappers.positionMapper.get(entity);

                    gc.shader.bind();
                    if (mc != null && mc.orientation == MovementComponent.LEFT) {
                        // Set the mirrored projection matrix
                        gc.shader.setUniformMat4f("pr_matrix", cc.projectionMatrixHorizontalFlip);
                        // Since player aligns with center screen with its bottom left corner, we need to temporarily
                        // also offset the view matrix.
                        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
                        Vector3f currentTranslation = cc.viewMatrix.getTranslation();
                        gc.shader.setUniformMat4f("vw_matrix",
                                Matrix4f.translate(currentTranslation.add(new Vector3f(dc.dimension.x, 0.0f, 0.0f)))
                        );
                    }

                    // Set uniforms
                    gc.shader.setUniformMat4f("md_matrix", Matrix4f.translate(pc.position)); // Tmp fix for giving correct positions to vertices in the vertexbuffers
                    gc.shader.setUniform1f("tex", gc.texture.getTextureID()); // Specify which texture slot to use

                    // Bind texture and specify texture slot
                    gc.texture.bind();
                    glActiveTexture(gc.texture.getTextureID());

                    // Render!
                    gc.geometry.render();

                    // Restore the default projection and view matrices
                    gc.shader.setUniformMat4f("pr_matrix", cc.projectionMatrix);
                    gc.shader.setUniformMat4f("vw_matrix", cc.viewMatrix);
                } else if (entity instanceof HierarchicalPlayer) {
                    // Get components via mapper for O(1) component retrieval
                    pc = Mappers.positionMapper.get(entity);
                    mc = Mappers.movementMapper.get(entity);
                    gc = Mappers.graphicsMapper.get(entity);
                    if (entity instanceof Ghost) {
                        // do nothing
                    } else {
                        // Extract the score component, we've encountered the player! For later use...
                        sc = Mappers.scoreMapper.get(entity);
                    }

                    if (mc.orientation == MovementComponent.LEFT) {
                        gc.shader.bind();
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
                        gc.handleColorMask();

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

                    gc.render(pc.position);
                }
            }
        }

        if (!Main.loading) {
            // Draw all the health bars in the currently active module for all entities which have a HealthComponent.
            // Dead entities are automatically removed from the engine, and hence also their healthbars.
            this.drawHealthBars();

            if (sc != null) {
                this.drawScore(sc.getScore(), cc.viewMatrix.getTranslation().sub(new Vector3f(Main.SCREEN_WIDTH / 4, Main.SCREEN_HEIGHT / 4, 0.0f)));
            }
        }

        this.drawOverlays((Camera) camera);


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

                if (a instanceof HierarchicalPlayer) {
                    DebugUtils.drawCircle(a.getComponent(PositionComponent.class).position.add(new Vector3f(a.getComponent(DimensionComponent.class).dimension.x / 2, 0.8f, 0.0f)), 0.9f, 50);
                }
            }

            DebugUtils.flush();
        }
        glfwSwapBuffers(Main.window); // swap the color buffers
    }

    private void drawOverlays(Camera camera) {
        Vector3f dimension = new Vector3f(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, 0.0f); // Fullscreen

        if (this.loadingScreen == null) {
            this.loadingScreen = new GraphicsComponent(Shader.SIMPLE, Texture.LOADING, dimension, RenderLayer.LOADING_SCREEN, false);
        }

        if (this.vignette == null) {
            this.vignette = new GraphicsComponent(Shader.SIMPLE, Texture.VIGNETTE_OVERLAY, dimension, RenderLayer.VIGNETTE, false);
        }

        if (this.noise == null) {
            this.noise = new GraphicsComponent(Shader.SIMPLE, Texture.NOISE_OVERLAY, dimension, RenderLayer.NOISE, false);
        }

        Vector3f position = camera.getComponent(PositionComponent.class).position.sub(dimension.scale(0.5f));
        if (Main.loading) {
            this.loadingScreen.render(position);
        } else {
            this.vignette.render(position);
            this.noise.render(position);
        }
    }

    /**
     * Draws the score of the player
     */
    private void drawScore(int score, Vector3f position) {
        String text = "Score: " + String.format("%05d", score);
        this.textGenerator.drawText(text, -position.x + 3.5f, -position.y + 4.0f);
    }

    /**
     * Draw the health bars
     */
    private void drawHealthBars() {
        // Ad hoc way of drawing the healthbars
        glClear(GL_DEPTH_BUFFER_BIT); // Allows drawing on top of all the other stuff
        PositionComponent pc;
        DimensionComponent dc;
        HealthComponent hc;
        for (Entity entity : this.entities) {
            hc = Mappers.healthMapper.get(entity);
            if (hc != null && !(entity instanceof Ghost)) {
                pc = Mappers.positionMapper.get(entity);
                dc = Mappers.dimensionMapper.get(entity);

                Vector3f fullBarSize = new Vector3f(1.0f, 0.1f, 0.0f);
                Vector3f healthBarSize = new Vector3f(hc.health / (float) hc.initialHealth, 0.1f, 0.0f);
                // Foreground (GREEN)
                this.drawBar(
                        pc.position.add(new Vector3f(dc.dimension.x / 2.0f, 1.1f * dc.dimension.y, 0.0f)),
                        healthBarSize,
                        Texture.GREEN
                );

                // Background (RED)
                this.drawBar(
                        pc.position.add(new Vector3f(dc.dimension.x / 2.0f, 1.1f * dc.dimension.y, 0.0f)),
                        fullBarSize,
                        Texture.RED
                );
            }
        }
    }

    private void drawBar(Vector3f position, Vector3f dimension, Texture texture) {
        GraphicsComponent bar = new GraphicsComponent(Shader.SIMPLE, texture, dimension, true);
        bar.flush(position);
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
