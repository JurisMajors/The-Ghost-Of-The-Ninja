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
    private int currentFrame;
    private int pressEnterCycle;
    private boolean pressEnterVisible;

    public StartScreen() {
        this.currentFrame = -120; // Dirty hack to create an initial delay like this
        this.pressEnterCycle = 60;
        this.pressEnterVisible = false;

        this.entities = new ArrayList<>();

        // PRESS_ENTER must be the LAST entity to be added. Because of hacky workaround that saves me time :*)
        this.entities.add(
                new ArtTile(
                        Texture.PRESS_ENTER,
                        RenderLayer.MAIN
                )
        );

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
                        Texture.LOGO_FULLSCREEN,
                        RenderLayer.MAIN
                )
        );

        this.load();

        // PRESS_ENTER should initially be hidden
        TheEngine.getInstance().removeEntity(
                this.entities.get(0) // The dirty timesaving thing
        );

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

        // Logic for the blinking PRESS ENTER text
        this.currentFrame++;
        if (this.currentFrame >= this.pressEnterCycle) {
            this.currentFrame = 0;
            this.pressEnterVisible = !this.pressEnterVisible;

            if (this.pressEnterVisible) { // If it should be visible, add it!
                TheEngine.getInstance().addEntity(
                        this.entities.get(0) // The dirty timesaving thing
                );
            } else { // Otherwise remove it again
                TheEngine.getInstance().removeEntity(
                        this.entities.get(0) // The dirty timesaving thing
                );
            }
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
