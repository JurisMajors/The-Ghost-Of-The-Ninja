package group4.AI;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.identities.GhostComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.entities.world.Exit;
import group4.ECS.etc.Families;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.CameraSystem;
import group4.ECS.systems.RenderSystem;
import group4.ECS.systems.collision.CollisionEventSystem;
import group4.ECS.systems.collision.CollisionSystem;
import group4.ECS.systems.collision.UncollidingSystem;
import group4.ECS.systems.death.GhostDyingSystem;
import group4.ECS.systems.movement.GhostMovementSystem;
import group4.game.Main;
import group4.game.Timer;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.levels.AILevel;
import group4.maths.Vector3f;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.glClear;

/**
 * Evaluates individuals of the population
 */
public class Evaluator implements FitnessEvaluator<Brain> {

    /** current Module to train AI on **/
    //private Module currModule;

    /** TODO: make timer singleton **/
    private Timer timer;

    /** the level (temporary) **/
    //public Level level;

    public Evaluator() {
        // init module
        this.timer = new Timer();
        // register systems to engine
        // initSystems(TheEngine.getInstance());
        if (Main.SHOULD_OPENGL) {
            Shader.loadAllShaders();
            Texture.loadAllTextures();
        }

    }

    @Override
    public double getFitness(Brain brain, List<? extends Brain> list) {
        Engine engine = TheEngine.getInstance();
        initSystems(engine);
        Level level = new AILevel();
        Module currModule = level.getCurrentModule();

        clearPlayers(engine, currModule);

        Vector3f startingPos = currModule.getPlayerInitialPosition();

        // create the ghost
        Entity ghost = new Ghost(
                new Vector3f(startingPos),
                level,
                brain);
        engine.addEntity(ghost);


        // while we did not exceed the timelimit, play the game
        double initTime = timer.getTime();
        while (true) {
            if (Evolver.render) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            }
            engine.update(1.0f);
            if (Evolver.render) {
                glfwSwapBuffers(Main.window); // swap the color buffers
            }
            // termination conditions
            if (ghost.getComponent(HealthComponent.class).health <= 0) {
                System.out.println("death");
                break;
            } else if (timer.getTime() - initTime >= Evolver.timelimit) {
                System.out.println("timeout");
                break;
            }
        }
        System.out.println(Arrays.toString(ghost.getComponent(GhostComponent.class).moveFreq));
        // unload the entities from the engine and delete them from the module reference
        Vector3f closestExit = null;
        Vector3f ghostPos = ghost.getComponent(PositionComponent.class).position;
        // get closest exit
        for (Exit exit : currModule.getExits()) {
            Vector3f exitPos = exit.getComponent(PositionComponent.class).position;
            float toExit = ghostPos.euclidDist(exitPos);

            if (closestExit == null) {
                closestExit = exitPos;
                continue;
            }
            float toClosest = ghostPos.euclidDist(closestExit);
            if (toExit < toClosest) {
                closestExit = exitPos;
            }

        }
        // fitness = if no exits exist, simply take x coordinate, otherwise the distance traveled in the direction of closest exit
        float fitness = closestExit == null ? ghostPos.x :
                Math.max(startingPos.euclidDist(closestExit) - ghostPos.euclidDist(closestExit), 0);
        currModule.unload();
        return fitness;
    }

    /**
     * True if high fitness is better
     */
    @Override
    public boolean isNatural() {
        return true;
    }

    /**
     * registers all necessary systems for running a minimal game for the AI
     */
    private void initSystems(Engine engine) {
//        System.out.println(engine);
        // clear all systems for robustness
        for (EntitySystem system : engine.getSystems()) {
            engine.removeSystem(system);
        }

        if (Evolver.render) {
            engine.addSystem(new CameraSystem(Families.ghostFamily));
        }
        engine.addSystem(new GhostMovementSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new CollisionEventSystem());
        engine.addSystem(new UncollidingSystem());
        engine.addSystem(new GhostDyingSystem());
        if (Evolver.render) {
            engine.addSystem(new RenderSystem());
        }
    }

    private void clearPlayers(Engine engine, Module cur) {
        // fetch gamestate, all entities which hold bounding box
        ImmutableArray<Entity> player = engine.getEntitiesFor(Families.playerFamily);
        ImmutableArray<Entity> lastGhost = engine.getEntitiesFor(Families.ghostFamily);
        List<Player> players = new ArrayList<>();

        // add the module entities except the player
        for (Entity entity : player) {
            players.add((Player) entity);
        }

        for (Entity entity : lastGhost) {
            players.add((Player) entity);
        }

        for (Player p : players) {
            cur.removeEntity(p);
            engine.removeEntity(p);
        }
    }

}
