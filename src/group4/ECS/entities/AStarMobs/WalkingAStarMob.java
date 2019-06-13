package group4.ECS.entities.AStarMobs;

import group4.ECS.components.GraphComponent;
import group4.ECS.components.identities.WalkingMobComponent;
import group4.ECS.systems.GraphHandlers.WalkingAStarMobGraphHandler;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class WalkingAStarMob extends AStarMob {
    public WalkingAStarMob(Vector3f position, Level l, Module module, String inFile, String outFile, GraphComponent graphComponent) {
        super(position, l, module, inFile, outFile, WalkingAStarMobGraphHandler.getInstance(), graphComponent);
        this.add(new WalkingMobComponent());
    }

    public WalkingAStarMob(Vector3f position, Level l, Module module, String inFile, String outFile, Texture texture, float[] texCoords, GraphComponent graphComponent) {
        super(position, l, module, inFile, outFile, texture, texCoords, WalkingAStarMobGraphHandler.getInstance(), graphComponent);
        this.add(new WalkingMobComponent());
    }

    public WalkingAStarMob(Vector3f position, Level l, Module module, Texture texture, float[] texCoords, GraphComponent graphComponent) {
        this(position, l, module, null, null, texture, texCoords, graphComponent);
    }

    public static String getName() {
        return "WalkingMob";
    }
}
