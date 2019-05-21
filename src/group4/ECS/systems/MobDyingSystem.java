package group4.ECS.systems;

import com.badlogic.ashley.core.Family;
import group4.ECS.etc.Families;

public class MobDyingSystem extends PlayerDyingSystem {

    MobDyingSystem(Family f) {
        super(f, false);
    }

    public MobDyingSystem() {
        this(Families.mobFamily);
    }

}
