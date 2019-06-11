package group4.ECS.entities.AStarMobs;

import group4.ECS.components.identities.JumpingWalkingMobComponent;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.systems.GraphHandlers.JumpingAStarMobGraphHandler;
import group4.ECS.systems.GraphHandlers.JumpingWalkingAStarMobGraphHandler;
import group4.ECS.systems.movement.MovementHandlers.JumpingWalkingMobMovementHandler;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class JumpingWalkingAStarMob extends AStarMob {
    public JumpingWalkingAStarMob(Vector3f position, Level l, Module module, String inFile, String outFile) {
        super(position, l, module, inFile, outFile, JumpingWalkingAStarMobGraphHandler.getInstance());
        this.add(new JumpingWalkingMobComponent());
    }

    public JumpingWalkingAStarMob(Vector3f pos, Level l, Module module, String inFile, String outFile, Texture tex, float[] texCoords) {
        super(pos, l, module, inFile, outFile, tex, texCoords, JumpingWalkingAStarMobGraphHandler.getInstance());
        this.add(new JumpingWalkingMobComponent());
    }

    public static String getName() {
        return "JumpingWalkingAStarMob";
    }
}
