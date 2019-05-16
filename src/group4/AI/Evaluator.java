package group4.AI;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.GhostComponent;
import group4.ECS.components.HealthComponent;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.etc.Families;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.GhostDyingSystem;
import group4.ECS.systems.GhostMovementSystem;
import group4.ECS.systems.MovementSystem;
import group4.ECS.systems.RenderSystem;
import group4.ECS.systems.collision.CollisionEventSystem;
import group4.ECS.systems.collision.CollisionSystem;
import group4.ECS.systems.collision.UncollidingSystem;
import group4.game.Timer;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.ModuleFactory;
import group4.levelSystem.levels.SimpleLevel;
import group4.levelSystem.levels.TestLevel;
import group4.maths.Vector3f;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.Arrays;
import java.util.List;

/**
 * Evaluates individuals of the population
 */
public class Evaluator implements FitnessEvaluator<Brain> {

    /** current Module to train AI on **/
    private Module currModule;

    /** TODO: make timer singleton **/
    private Timer timer;

    /** the level (temporary) **/
    public Level level;

    public Evaluator() {
        // init module
        this.timer = new Timer();
        // register systems to engine
        initSystems();
        level = new TestLevel();
        this.currModule = this.level.getCurrentModule();

    }

    @Override
    public double getFitness(Brain brain, List<? extends Brain> list) {
        Engine engine = TheEngine.getInstance();
        // fetch gamestate, all entities which hold bounding box
        ImmutableArray<Entity> gamestate = engine.getEntitiesFor(Families.gamestateFamily);

        Entity p = null;

        // add the module entities except the player
        for (Entity entity : gamestate) {
            if (entity instanceof Player) {
                p = entity;
                break; // dont add the player
            }
        }
        this.currModule.removeEntity(p);
        engine.removeEntity(p);
        // create the ghost
        Entity ghost = new Ghost(this.currModule.getPlayerInitialPosition(),
                this.level,
                brain);
        engine.addEntity(ghost);

        // while we did not exceed the timelimit, play the game
        double initTime = timer.getTime();
        while (true) {
            TheEngine.getInstance().update(1.0f/60.0f);
            if (ghost.getComponent(HealthComponent.class).health <= 0) {
                System.out.println("death");
                break;
            } else if (timer.getTime() - initTime >= Evolver.timelimit) {
                System.out.println("timeout");
                break;
            }
        }
        System.out.println(Arrays.toString(ghost.getComponent(GhostComponent.class).moveFreq));
        // create new module
        this.currModule.reset();
        return ghost.getComponent(PositionComponent.class).position.x;
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
    private void initSystems() {

        // clear all systems for robustness
        for (EntitySystem system : TheEngine.getInstance().getSystems()) {
            TheEngine.getInstance().removeSystem(system);
        }

//        Shader.loadAllShaders();
//        Texture.loadAllTextures();

        Engine engine = TheEngine.getInstance();

        engine.addSystem(new GhostMovementSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new CollisionEventSystem());
        engine.addSystem(new UncollidingSystem());
        engine.addSystem(new GhostDyingSystem());
        //engine.addSystem(new RenderSystem());
    }

}
