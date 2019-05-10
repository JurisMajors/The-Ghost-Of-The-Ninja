package group4.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.CameraComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.entities.Camera;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.game.Main;
import group4.maths.Matrix4f;

import static org.lwjgl.opengl.GL41.GL_TEXTURE1;
import static org.lwjgl.opengl.GL41.glActiveTexture;

/**
 * This system renders all entities which are in the graphicsFamily, thus having a
 * position as well as a graphics component
 */
public class CameraSystem extends EntitySystem {

    // array of registered entities in the graphicsFamily
    private ImmutableArray<Entity> entities;

    public CameraSystem() {}

    /**
     * @param priority used for creating a systems pipeline (lowest prio gets executed first)
     */
    public CameraSystem(int priority) {}

    /**
     * when added to engine, this function will register all entities in the graphicsFamily
     * @param engine a single instance throughout program which manages ECS
     */
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Families.cameraFamily);
    }

    /**
     * Do something when this system gets removed from engine
     * @param engine a single instance throughout program which manages ECS
     */
    public void removedFromEngine(Engine engine) {}

    /**
     * The heart piece of a system. This method gets called on each
     * update on the single instance of the engine
     * @param deltaTime time between last and current update
     */
    public void update(float deltaTime) {
        // Get the camera and the player (there should be only one of both, currently)
        Entity main_camera = entities.get(0);
        Entity player = TheEngine.getInstance().getEntitiesFor(Families.Player).get(0);

        // get mapper for O(1) component retrieval
        CameraComponent cc = Mappers.cameraMapper.get(main_camera);
        PositionComponent pc = Mappers.positionMapper.get(player);

        // Update the view matrix to be the player position
        cc.view_matrix = Matrix4f.translate(pc.position);
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