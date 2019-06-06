package group4.UI;

import com.badlogic.ashley.core.Entity;
import group4.ECS.entities.world.ArtTile;
import group4.ECS.etc.TheEngine;
import group4.game.GameState;
import group4.game.Main;
import group4.graphics.RenderLayer;
import group4.graphics.Texture;
import group4.input.KeyBoard;
import group4.levelSystem.FileLevel;
import group4.levelSystem.Level;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class StartScreen {
    private List<Entity> entities;

    public StartScreen() {
        this.entities = new ArrayList<>();

        this.entities.add(
                new ArtTile(
                        Texture.START_BG,
                        RenderLayer.BACKGROUND
                )
        );

        this.entities.add(
                new ArtTile(
                        Texture.VIGNETTE_OVERLAY,
                        RenderLayer.VIGNETTE
                )
        );

        this.entities.add(
                new ArtTile(
                        Texture.NOISE_OVERLAY,
                        RenderLayer.NOISE
                )
        );

        this.entities.add(
                new ArtTile(
                        Texture.PRESS_ENTER,
                        RenderLayer.MAIN
                )
        );

        this.load();

        // And finally update the gamestate
        Main.setState(GameState.STARTSCREEN);
    }

    private void playGame() {
        System.out.println("PLAYGAME!");

        // Immediately unload this startscreen and initialize the level
        this.unload();
        new FileLevel("./src/group4/res/maps/level_02");
    }

    public void update() {
        if (KeyBoard.isKeyDown(GLFW_KEY_ENTER)) {
            this.playGame();
        }
    }

    private void load() {
        for (Entity entity : this.entities) {
            TheEngine.getInstance().addEntity(entity);
        }
    }

    private void unload() {
        for (Entity entity : this.entities) {
            TheEngine.getInstance().removeEntity(entity);
        }
    }
}
