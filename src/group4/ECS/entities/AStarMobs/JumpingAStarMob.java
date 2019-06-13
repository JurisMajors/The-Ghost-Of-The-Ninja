package group4.ECS.entities.AStarMobs;

import group4.ECS.components.GraphComponent;
import group4.ECS.components.identities.JumpingAStarMobComponent;
import group4.ECS.systems.GraphHandlers.JumpingAStarMobGraphHandler;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class JumpingAStarMob extends AStarMob {
    public JumpingAStarMob(Vector3f position, Level l, Module module, String inFile, String outFile, GraphComponent graphComponent) {
        super(position, l, module, inFile, outFile, JumpingAStarMobGraphHandler.getInstance(), graphComponent);
        this.add(new JumpingAStarMobComponent());
    }

    public JumpingAStarMob(Vector3f position, Level l, Module module, String inFile, String outFile, Texture tex, float[] texCoords, GraphComponent graphComponent) {
        super(position, l, module, inFile, outFile, tex, texCoords, JumpingAStarMobGraphHandler.getInstance(), graphComponent);
        this.add(new JumpingAStarMobComponent());
    }

    public JumpingAStarMob(Vector3f position, Level l, Module module, Texture tex, float[] texCoords, GraphComponent graphComponent) {
        this(position, l, module, null, null, tex, texCoords, graphComponent);
    }

    public static String getName() {
        return "JumpingMob";
    }
}
