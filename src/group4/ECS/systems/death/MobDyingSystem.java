package group4.ECS.systems.death;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import group4.ECS.etc.Families;
import group4.audio.Sound;

public class MobDyingSystem extends PlayerDyingSystem {

    MobDyingSystem(Family f, int priority) {
        super(f, false, priority);
    }

    public MobDyingSystem(int priority) {
        this(Families.mobFamily, priority);
    }

    @Override
    protected boolean die(Entity entity, float deltaTime) {
        super.die(entity, deltaTime);
        return true;
    }

    @Override
    protected void sound() {
        super.sound();
        Sound.playRandom(Sound.MOBDIE);
    }

}
