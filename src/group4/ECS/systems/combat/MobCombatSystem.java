package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.etc.EntityConst;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;

public class MobCombatSystem extends IteratingSystem {

    public MobCombatSystem(int priority) {
        super(Families.mobFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MobComponent mobc = Mappers.mobMapper.get(entity);

        if (mobc.state.equals(EntityConst.MobState.ATTACKING)) {

        } else {
            // don't attack
        }
    }

}
