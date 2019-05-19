package group4.levelSystem.modules;

import group4.ECS.entities.Background;
import group4.ECS.entities.Camera;
import group4.ECS.entities.world.Block;
import group4.ECS.entities.world.Platform;
import group4.ECS.entities.world.SplinePlatform;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.TileMapping;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

public class TestModule extends Module {

    public TestModule(Level l) {
        super(l);
    }

    public TestModule(Level l, String modelName) {
        super(l, modelName);
    }

    @Override
    protected void constructModule() {
        // TODO: This is a bad spot for this, but it demonstrates the functionality. Please move.
        Camera camera = new Camera();
        this.addEntity(camera); // Adding the camera to the module (which adds it to the engine?)

        Background background = new Background(
                new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f(35.0f, 12.0f, 0.0f),
                Shader.SIMPLE,
                Texture.BACKGROUND);
        this.addEntity(background);

        // TODO: Change to platform entities when these work
        // Construct the test module for training the neural network

        // Add the LHS wall
        for (int i = 0; i < 10; i++) {
            this.addEntity(
                    new Block(
                            new Vector3f(0.0f, 2.0f + i * 1.0f, 0.0f),
                            Shader.SIMPLE,
                            Texture.MAIN_TILES,
                            TileMapping.MAIN.WALL_LEFT1
                    )
            );
        }

        // Add floors on which player will stand initially
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 1 && j == 0) {
                    this.addEntity(
                            new Block(
                                    new Vector3f(j * 1.0f, i * 1.0f, 0.0f),
                                    Shader.SIMPLE,
                                    Texture.MAIN_TILES,
                                    TileMapping.MAIN.WALL_CORNER_LEFT1
                            )
                    );
                } else {
                    this.addEntity(
                            new Block(
                                    new Vector3f(j * 1.0f, i * 1.0f, 0.0f),
                                    Shader.SIMPLE,
                                    Texture.MAIN_TILES,
                                    TileMapping.MAIN.FLOOR1
                            )
                    );
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                this.addEntity(
                        new Block(
                                new Vector3f(19.0f + j * 1.0f, i * 1.0f, 0.0f),
                                Shader.SIMPLE,
                                Texture.BRICK
                        )
                );
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                this.addEntity(
                        new Block(
                                new Vector3f(25.0f + j * 1.0f, i * 1.0f, 0.0f),
                                Shader.SIMPLE,
                                Texture.BRICK
                        )
                );
            }
        }


        // Add some blocks to potentially confuse the player on initial floor
        this.addEntity(
                new Block(
                        new Vector3f(1.0f, 2.0f, 0.0f),
                        Shader.SIMPLE,
                        Texture.BRICK
                )
        );

        this.addEntity(
                new Block(
                        new Vector3f(5.0f, 2.0f, 0.0f),
                        Shader.SIMPLE,
                        Texture.BRICK
                )
        );

        // Create semi-stair like thing
        for (int i = 0; i < 3; i++) {
            this.addEntity(
                    new Block(
                            new Vector3f(9.0f + i * 1.0f, 2.0f, 0.0f),
                            Shader.SIMPLE,
                            Texture.MAIN_TILES,
                            TileMapping.MAIN.FLOOR1                    )
            );
        }

        for (int i = 0; i < 3; i++) {
            this.addEntity(
                    new Block(
                            new Vector3f(11.0f + i * 1.0f, 3.0f, 0.0f),
                            Shader.SIMPLE,
                            Texture.MAIN_TILES,
                            TileMapping.MAIN.FLOOR1                    )
            );
        }

        for (int i = 0; i < 3; i++) {
            this.addEntity(
                    new Block(
                            new Vector3f(13.0f + i * 1.0f, 4.0f, 0.0f),
                            Shader.SIMPLE,
                            Texture.MAIN_TILES,
                            TileMapping.MAIN.FLOOR1                    )
            );
        }




        // Create height increase to module end exit
        for (int i = 0; i < 3; i++) {
            this.addEntity(
                    new Block(
                            new Vector3f(29.0f + i * 1.0f, 2.0f, 0.0f),
                            Shader.SIMPLE,
                            Texture.MAIN_TILES,
                            TileMapping.MAIN.FLOOR1                    )
            );
        }

        for (int i = 0; i < 4; i++) {
            this.addEntity(
                    new Block(
                            new Vector3f(31.0f + i * 1.0f, 3.0f, 0.0f),
                            Shader.SIMPLE,
                            Texture.MAIN_TILES,
                            TileMapping.MAIN.FLOOR1                    )
            );
        }

        // Create the exit
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.addEntity(
                        new Block(
                                new Vector3f(33.0f + i * 1.0f, 4.0f + j * 1.0f, 0.0f),
                                Shader.SIMPLE,
                                Texture.EXIT
                        )
                );       // TODO: Change to exit entity, but first need to know how collision detection is going to work to detect if player overlaps an exit, before I create an exit entity
            }
        }
    }

    @Override
    protected Vector3f getStartScreenWindow() {
        return new Vector3f();
    }

    @Override
    public Vector3f getPlayerInitialPosition() {
        return new Vector3f(6.0f, 4.0f, 0.0f);
    }
}

