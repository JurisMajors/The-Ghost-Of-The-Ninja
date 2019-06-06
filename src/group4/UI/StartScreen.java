package group4.UI;

import com.badlogic.ashley.core.Entity;
import group4.input.KeyBoard;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class StartScreen {
    private List<Entity> entities;

    public StartScreen() {
        this.entities = new ArrayList<>();
    }

    private void playGame() {
        System.out.println("PLAYGAME!");
    }

    public void update() {
        if (KeyBoard.isKeyDown(GLFW_KEY_ENTER)) {
            this.playGame();
        }
    }
}
