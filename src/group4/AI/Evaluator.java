package group4.AI;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.etc.Families;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.MovementSystem;
import group4.game.Timer;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.ModuleFactory;
import group4.levelSystem.levels.SimpleLevel;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

/**
 * Evaluates individuals of the population
 */
public class Evaluator implements FitnessEvaluator<Brain> {

    /** current Module to train AI on **/
    private Module currModule;

    /** TODO: make timer singleton **/
    private Timer timer;

    public Evaluator() {
        // init module
        this.timer = new Timer();

        // register systems to engine
        initSystems();
    }

    @Override
    public double getFitness(Brain brain, List<? extends Brain> list) {
        this.currModule = ModuleFactory.createModule(Evolver.level);

        // fetch gamestate, all entities which hold bounding box
        ImmutableArray<Entity> gamestate = TheEngine.getInstance().getEntitiesFor(Families.gamestateFamily);

        // reinit gamestate
        TheEngine.getInstance().removeAllEntities();
        for (Entity entity : gamestate) {
            TheEngine.getInstance().addEntity(entity);
        }

        //TODO: Create ghost player, play a game with it, evaluate results
        double initTime = timer.getTime();

        // while we did not exceed the timelimit, play the game
        while (timer.getTime() - initTime <= Evolver.timelimit) {
            TheEngine.getInstance().update((float) timer.getDeltaTime());
        }

        // clear engine
        TheEngine.getInstance().removeAllEntities();

        return 0;
    }

    /**
     * True if high fitness is better
     */
    @Override
    public boolean isNatural() {
        return false;
    }

    /**
     * registers all necessary systems for running a minimal game for the AI
     */
    private void initSystems() {

        // clear all systems for robustness
        for (EntitySystem system : TheEngine.getInstance().getSystems()) {
            TheEngine.getInstance().removeSystem(system);
        }

        // TODO: register necessary systems
        TheEngine.getInstance().addSystem(new MovementSystem());
    }

}
