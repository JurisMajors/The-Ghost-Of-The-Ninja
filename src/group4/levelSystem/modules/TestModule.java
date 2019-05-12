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

        // TODO: Change to platform entities when these work
        // Construct the test module for training the neural network

        // Add the LHS wall
        Vector3f tempPosition = new Vector3f();
        Vector3f tempDimension = new Vector3f(2.0f, 28.0f, 0.0f);
        Block LHSWall = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(LHSWall);

        // Add floor on which player will stand initially
        tempPosition = new Vector3f(2.0f, 0.0f, 0.0f);
        tempDimension = new Vector3f(20.0f, 2.0f, 0.0f);
        Block initFloor = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(initFloor);

        // Add some blocks to potentially confuse the player on initial floor
        tempPosition = new Vector3f(2.0f, 2.0f, 0.0f);
        tempDimension = new Vector3f(2.0f, 2.0f, 0.0f);
        Block confusion = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(confusion);

        // Add a block for the player to jump over
        tempPosition = new Vector3f(9.0f, 2.0f, 0.0f);
        tempDimension = new Vector3f(2.0f, 2.0f, 0.0f);
        Block jump = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(jump);

        // Create semi-stair like thing
        tempPosition = new Vector3f(22.0f, 2.0f, 0.0f);
        tempDimension = new Vector3f(5.0f, 2.0f, 0.0f);
        Block stairlike = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(stairlike);

        tempPosition = new Vector3f(27.0f, 4.0f, 0.0f);
        tempDimension = new Vector3f(8.0f, 2.0f, 0.0f);
        stairlike = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(stairlike);

        tempPosition = new Vector3f(35.0f, 6.0f, 0.0f);
        tempDimension = new Vector3f(5.0f, 2.0f, 0.0f);
        stairlike = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(stairlike);

        // Create floor to jump on to
        tempPosition = new Vector3f(44.0f, 0.0f, 0.0f);
        tempDimension = new Vector3f(8.0f, 2.0f, 0.0f);
        Block jumpFloor = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(jumpFloor);

        // Create a hole to jump over
        tempPosition = new Vector3f(54.0f, 0.0f, 0.0f);
        tempDimension = new Vector3f(6.0f, 2.0f, 0.0f);
        Block afterHole = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(afterHole);

        // Create height increase to module end exit
        tempPosition = new Vector3f(60.0f, 2.0f, 0.0f);
        tempDimension = new Vector3f(4.0f, 2.0f, 0.0f);
        Block exitStairs = new Block(tempPosition, tempDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(exitStairs);

        // Create the exit
        Vector3f exitPosition = new Vector3f(62.0f, 4.0f, 0.0f);
        Vector3f exitDimension = new Vector3f(2.0f, 2.0f, 0.0f);
        Block exit = new Block(exitPosition, exitDimension, Shader.SIMPLE, Texture.DEBUG);
        this.addEntity(exit);
        // TODO: Change to exit entity, but first need to know how collision detection is going to work to detect if player overlaps an exit, before I create an exit entity
    }

    @Override
    protected Vector3f getStartScreenWindow() {
        return new Vector3f();
    }

    @Override
    public Vector3f getPlayerInitialPosition() {
        return new Vector3f(3.0f, 2.0f, 0.0f);
    }
}
