package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;
import group4.ECS.etc.EntityConst;
import group4.ECS.systems.movement.MovementHandlers.AbstractMovementHandler;

public class MobComponent implements Component {

    public float currentVisionRange = 4.0f;
    public final static float chaseRange = 8.0f;
    public final static float viewRange = 6.0f;
    public float attackRange = 6.0f;
    public EntityConst.MobState state;

    public AbstractMovementHandler handler;

    public MobComponent(AbstractMovementHandler handler) {
        this.handler = handler;
        this.state = EntityConst.MobState.DEFAULT;
    }

    public MobComponent() {

    }

    public MobComponent(AbstractMovementHandler handler, float attackRange) {
        this.handler = handler;
        this.attackRange = attackRange;
        this.state = EntityConst.MobState.DEFAULT;
    }

}
