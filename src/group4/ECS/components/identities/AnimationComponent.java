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
    private Map<EntityState, Animation> animations;
    private Animation currentAnimation;
    private EntityState currentState;
    /**
     * Default constructor which creates the component. No data is currently associated with being animatable.
     */
    public AnimationComponent() {
        // Initialize animations and set it to the NONE animation, which is NULL
        animations = new HashMap<>();
        addAnimation(EntityState.NONE, null);
        setAnimation(EntityState.NONE);
        this.currentState = EntityState.NONE;
    }

    public void addAnimation(EntityState state, Animation anim) {
        animations.put(state, anim);
    }

    public void setAnimation(EntityState state) {
        if (state != this.currentState) {
            //        for (EntityState name: this.animations.keySet()){
//            Animation value = this.animations.get(name);
//            System.out.println(name + " : " + value);
//        }
            this.currentState = state;
//            System.out.println("[INFO] Setting " + state);
            if (animations.containsKey(state)) {
                currentAnimation = animations.get(state);
            } else {
                System.err.println("[WARNING] Animation '" + state + "' not found.");
            }
        }
    }

    public Animation getCurrentAnimation() {
        return this.currentAnimation;
    }

    public Animation getAnimation(EntityState state) {
        return this.animations.get(state);
    }
}
