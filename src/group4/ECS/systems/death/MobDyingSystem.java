package group4.ECS.systems.death;

import com.badlogic.ashley.core.Family;
import group4.ECS.etc.Families;

public class MobDyingSystem extends PlayerDyingSystem {

    MobDyingSystem(Family f, int priority) {
        super(f, false, priority);
    }

    public MobDyingSystem(int priority) {
        this(Families.mobFamily, priority);
    }

}
