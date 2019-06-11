package group4.ECS.entities.AStarMobs;

import group4.ECS.components.identities.JumpingAStarMobComponent;
import group4.ECS.components.identities.JumpingMobComponent;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.systems.GraphHandlers.JumpingAStarMobGraphHandler;
import group4.ECS.systems.movement.MovementHandlers.JumpingMobMovementHandler;
import group4.ECS.systems.movement.MovementHandlers.JumpingWalkingMobMovementHandler;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class JumpingAStarMob extends AStarMob {
    public JumpingAStarMob(Vector3f position, Level l, Module module, String inFile, String outFile) {
        super(position, l, module, inFile, outFile, JumpingAStarMobGraphHandler.getInstance());
        this.add(new JumpingAStarMobComponent());
    }

    public JumpingAStarMob(Vector3f position, Level l, Module module, String inFile, String outFile, Texture tex, float[] texCoords) {
        super(position, l, module, inFile, outFile, tex, texCoords, JumpingAStarMobGraphHandler.getInstance());
        this.add(new JumpingAStarMobComponent());
    }

    public static String getName() {
        return "JumpingAStarMob";
    }
}
