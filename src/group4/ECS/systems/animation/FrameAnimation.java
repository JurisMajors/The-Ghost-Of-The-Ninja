package group4.ECS.systems.animation;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.etc.Mappers;
import group4.graphics.ImageSequence;

public class FrameAnimation extends Animation {
    private ImageSequence sequence;
    private int fps; // assuming we're running at 60, store the fps for the animation.
    // I.e. we can show a 30fps animation in 60fps by holding every animation frame for 2 game-time frames.
    private float scaledT;

    public FrameAnimation(Entity target, float offsetT, ImageSequence sequence, int fps) {
        super(target, offsetT);
        this.sequence = sequence;
        this.fps = fps;
        this.scaledT = 0.0f;
    }

    @Override
    protected void stepAnimation(float deltaTime) {
        this.scaledT += deltaTime * this.fps / 60.0f; // Scale to account for target FPS. FPS in our game is fixed, hence hardcoded 60.
        this.scaledT %= 1.0f; // cap between 0 and 1.
        double t = (double) (this.scaledT / (1.0f / this.sequence.frameCount));
        int frame = Math.min((int) Math.floor(t), this.sequence.frameCount - 1); // Math.min gives us some safety :*)

        GraphicsComponent gcTarget = Mappers.graphicsMapper.get(target);
        gcTarget.setTexture(this.sequence.frames.get(frame));
    }
}
