package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;
import group4.ECS.etc.EntityState;
import group4.ECS.systems.animation.Animation;

import java.util.HashMap;
import java.util.Map;

/**
 * AnimationComponent, needed if the object needs to be registered as an Entity for the animation system.
 */
public class AnimationComponent implements Component {
    Map<EntityState, Animation> animations;
    Animation currentAnimation;
    /**
     * Default constructor which creates the component. No data is currently associated with being animatable.
     */
    public AnimationComponent() {
        animations = new HashMap<>();
        currentAnimation = EntityState.DEFAULT:
    }

    public void addAnimation(EntityState state, Animation anim) {
        animations.put(state, anim);
    };

    public void setAnimation(EntityState state) {
        if (animations.containsKey(state)) {

        }
    }

}
