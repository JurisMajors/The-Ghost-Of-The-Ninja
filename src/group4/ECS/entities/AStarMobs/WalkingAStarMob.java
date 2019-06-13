package group4.ECS.entities.AStarMobs;

import group4.ECS.components.GraphComponent;
import group4.ECS.components.identities.WalkingMobComponent;
import group4.ECS.entities.items.weapons.MobMeleeAttack;
import group4.ECS.systems.GraphHandlers.WalkingAStarMobGraphHandler;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class WalkingAStarMob extends AStarMob {
    private static float attackRange = 0.7f;
    private static MobMeleeAttack wpn = new MobMeleeAttack(10, 3.0f,
            new Vector3f(1f, 1f, 0f),
            new Vector3f(0.1f, 0.3f, 0.0f));

    public WalkingAStarMob(Vector3f position, Level l, GraphComponent graphComponent) {
        super(position, l, WalkingAStarMobGraphHandler.getInstance(), graphComponent,
                attackRange, wpn);
        this.add(new WalkingMobComponent());
    }

    public WalkingAStarMob(Vector3f position, Level l, Texture texture, float[] texCoords, GraphComponent graphComponent) {
        super(position, l, texture, texCoords, WalkingAStarMobGraphHandler.getInstance(), graphComponent,
                attackRange, wpn);
        this.add(new WalkingMobComponent());
    }

    public static String getName() {
        return "WalkingMob";
    }
}
