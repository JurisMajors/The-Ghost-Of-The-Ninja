package group4.ECS.systems.animation;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.etc.Mappers;
import group4.graphics.ImageSequence;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

public class FrameAnimation extends Animation {
    private ImageSequence sequence;
    private int fps; // assuming we're running at 60, store the fps for the animation.
    // I.e. we can show a 30fps animation in 60fps by holding every animation frame for 2 game-time frames.

    public FrameAnimation(Entity target, float offsetT, ImageSequence sequence, int fps) {
        super(target, offsetT);
        this.sequence = sequence;
        this.fps = fps; // TODO: all anims now take 1 second. fix.
    }

    @Override
    protected void stepAnimation() {
        int frame = Math.min(Math.round(this.currentT / (1.0f / sequence.frameCount)), this.sequence.frameCount - 1);
//        System.out.println(Math.round(this.currentT / (1.0f / sequence.frameCount)));
        // TODO: Fix frame calculation.. ZZzzZZzz....
        GraphicsComponent gcTarget = Mappers.graphicsMapper.get(target);
        gcTarget.setTexture(this.sequence.frames.get(frame));
    }
}
