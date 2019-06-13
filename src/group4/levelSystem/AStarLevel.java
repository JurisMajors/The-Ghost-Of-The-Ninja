package group4.levelSystem;

import group4.AI.Evolver;
import group4.ECS.entities.AStarMobs.JumpingAStarMob;
import group4.ECS.entities.AStarMobs.JumpingWalkingAStarMob;
import group4.ECS.entities.AStarMobs.WalkingAStarMob;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.entities.Player;
import group4.ECS.entities.world.Exit;
import group4.ECS.entities.world.SplinePlatform;
import group4.ECS.etc.TheEngine;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

/**
 * This is a level class which is designed for Ghost training purposes.
 * It is designed so that it only contains one module with no exit callbacks.
 */
public class AStarLevel extends Level {
    private final static String modulePath = Evolver.modulePath;

    @Override
    protected Module createRoot() {
        if (modulePath == null) throw new IllegalStateException("Module Path is not set for AI level");
        Module m = new Module(this, this.modulePath, null);
        // my little spline test
        Vector3f tempPosition = new Vector3f(15.0f, 4.0f, 0.0f);
        Vector3f tempDimension = new Vector3f(2.0f, 1.0f, 0.0f);
        float thickness = 0.4f;
        Vector3f[] tempPoint = new Vector3f[]{
                new Vector3f(0.0f, 0.0f, 0.0f).add(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(2.0f, 0.0f, 0.0f).add(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(2.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(4.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(4.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(6.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(6.0f, 0.0f, 0.0f).add(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(8.0f, 0.0f, 0.0f).add(new Vector3f(0.0f, thickness * 0.5f, 0.0f))
        };
        for (Vector3f v : tempPoint) {
            v.addi(new Vector3f(0.0f, 2.0f, 0.0f));
        }
        MultiSpline mySpline = new MultiSpline(tempPoint);
        SplinePlatform splinePlatform = new SplinePlatform(tempPosition, mySpline, thickness, Shader.SIMPLE, Texture.BRICK);
        m.addEntity(splinePlatform);
        return m;
    }

    @Override
    protected void createAdditionalModules() {

    }

    @Override
    protected Player createPlayer() {
        return new HierarchicalPlayer(new Vector3f(), this);
    }

    @Override
    protected void configExits() {
        Module mod = this.getCurrentModule();
        // my little spline test
        Vector3f tempPosition = new Vector3f(15.0f, 4.0f, 0.0f);
        Vector3f tempDimension = new Vector3f(2.0f, 1.0f, 0.0f);
        float thickness = 0.4f;
        Vector3f[] tempPoint = new Vector3f[]{
                new Vector3f(0.0f, 0.0f, 0.0f).add(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(2.0f, 0.0f, 0.0f).add(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(2.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(4.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(4.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(6.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(6.0f, 0.0f, 0.0f).add(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(8.0f, 0.0f, 0.0f).add(new Vector3f(0.0f, thickness * 0.5f, 0.0f))
        };
        for (Vector3f v : tempPoint) {
            v.addi(new Vector3f(0.0f, 2.0f, 0.0f));
        }
        MultiSpline mySpline = new MultiSpline(tempPoint);
        SplinePlatform splinePlatform = new SplinePlatform(tempPosition, mySpline, thickness, Shader.SIMPLE, Texture.BRICK);
        mod.addEntity(splinePlatform);

        ExitAction global = new ExitAction() {
            @Override
            public void exit() {
                System.out.println("Ghost has reached the exit");
            }
        };

        for (Module m : this.modules) {
            for (Exit e : m.getExits()) {
                this.setExitAction(e, global);
            }
        }
    }
}
