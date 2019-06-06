package group4.UI;

import com.badlogic.ashley.core.Entity;
import group4.game.GameState;
import group4.game.Main;
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
        Main.setState(GameState.STARTSCREEN);
    }

    private void playGame() {
        System.out.println("PLAYGAME!");
        // Immediately initialize the level
//        Level level = new FileLevel("./src/group4/res/maps/level_02");
    }

    public void update() {
        if (KeyBoard.isKeyDown(GLFW_KEY_ENTER)) {
            this.playGame();
        }
    }
}
