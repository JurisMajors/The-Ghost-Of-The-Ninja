package group4.levelSystem.modules;

import group4.ECS.entities.Camera;
import group4.ECS.entities.world.Block;
import group4.ECS.entities.world.Platform;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class TestModule extends Module {

    public TestModule(Level l) {
        super(l);
    }

    @Override
    protected void constructModule() {
        // TODO: This is a bad spot for this, but it demonstrates the functionality. Please move.
        Camera camera = new Camera();
        this.addEntity(camera); // Adding the camera to the module (which adds it to the engine?)

        // Construct the test module for training the neural network

        // Add the LHS wall
        Vector3f tempPosition = new Vector3f();
        Vector3f tempDimension = new Vector3f(1.0f, 14.0f, 0.0f);
        Block LHSWall = new Block
                (tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(LHSWall);




        // TODO: Add exit entity, but first need to know how collision detection is going to work to detect if player overlaps an exit
    }

    @Override
    protected Vector3f getStartScreenWindow() {
        return new Vector3f();
    }

    @Override
    public Vector3f getPlayerInitialPosition() {
        return new Vector3f(3.0f, 1.0f, 0.0f);
    }
}
