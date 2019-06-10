package group4.ECS.systems.animation;

import java.util.ArrayList;
import java.util.List;

public class AnimationSet extends Animation {

    // Store all Animations that belong to this AnimationSet
    protected List<Animation> animations;

    /**
     * Construct an empty animation set
     */
    public AnimationSet() {
        // Initialize parameters as null via Animation Constructor
        super(null, 0);

        // Initialise animation list
        this.animations = new ArrayList<>();
    }


    /**
     * Add an animation to this AnimationSet
     *
     * @param a The animation to add
     */
    public void add(Animation a) {
        this.animations.add(a);
    }


    /**
     * Update all animations in this set
     */
    @Override
    public boolean update(float deltaTime) {
        for (Animation a : this.animations) {
            a.update(deltaTime);
        }
        return true;
    }


    @Override
    protected void stepAnimation(float deltaTime) {
    } // Lekker leeg :)

}
