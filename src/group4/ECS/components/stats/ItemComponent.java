package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;

public class ItemComponent implements Component {

    public float cooldown;
    public float currCooldown;

    /**
     * standard constructor
     */
    public ItemComponent() {
        this.cooldown = 0.0f;
        this.currCooldown = 0.0f;
    }

    /**
     * @param cooldown the cooldown in sec to which currCooldown will be set after item was used
     */
    public ItemComponent(float cooldown) {
        this.cooldown = cooldown;
        this.currCooldown = 0.0f;
    }

}
