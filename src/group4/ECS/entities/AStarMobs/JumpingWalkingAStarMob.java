package group4.ECS.entities.AStarMobs;

import group4.ECS.components.GraphComponent;
import group4.ECS.components.identities.JumpingWalkingMobComponent;
import group4.ECS.entities.items.weapons.MobMeleeAttack;
import group4.ECS.systems.GraphHandlers.JumpingWalkingAStarMobGraphHandler;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class JumpingWalkingAStarMob extends AStarMob {
    private static float attackRange = 0.7f;
    private static MobMeleeAttack wpn = new MobMeleeAttack(5, 3.0f,
            new Vector3f(1,1,0), new Vector3f(0.1f, 0.3f, 0.0f));

    public JumpingWalkingAStarMob(Vector3f position, Level l, GraphComponent graphComponent) {
        super(position, l, JumpingWalkingAStarMobGraphHandler.getInstance(), graphComponent,
                attackRange, wpn);
        this.add(new JumpingWalkingMobComponent());
    }

    public JumpingWalkingAStarMob(Vector3f pos, Level l, Texture tex, float[] texCoords, GraphComponent graphComponent) {
        super(pos, l, tex, texCoords, JumpingWalkingAStarMobGraphHandler.getInstance(), graphComponent,
                attackRange, wpn);
        this.add(new JumpingWalkingMobComponent());
    }

    public static String getName() {
        return "JumpingWalkingMob";
    }
}
