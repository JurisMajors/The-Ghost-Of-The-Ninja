package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.etc.Families;

public class CombatEventSystem extends IteratingSystem {

    public CombatEventSystem() {
        super(Families.dmgReceivingFamily);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
