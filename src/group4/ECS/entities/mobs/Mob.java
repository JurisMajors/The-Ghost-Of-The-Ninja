package group4.ECS.entities.mobs;

import com.badlogic.ashley.core.Entity;
import group4.levelSystem.Level;

public class Mob extends Entity {

    public Level level;
    public Entity wpn;
    public float attackRange;

    public static String getName() {
        return "Mob";
    }
}
