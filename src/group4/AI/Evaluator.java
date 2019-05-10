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
import group4.levelSystem.levels.SimpleLevel;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

/**
 * Evaluates individuals of the population
 */
public class Evaluator implements FitnessEvaluator<Brain> {

    // current module on which the AI trains
    private Module currModule;

    // TODO: make timer singleton
    private Timer timer;

    // level
    private Level level;

    // TODO: find feasible time limit
    double timelimit;

    public Evaluator(Module currModule) {
        this.currModule = currModule;
        this.timer = new Timer();
        this.timelimit = 120.00;

        // temporary, for now we only have the simple level
        this.level = new SimpleLevel();

        initSystems();
    }

    @Override
    public double getFitness(Brain brain, List<? extends Brain> list) {

        // fetch gamestate, all entities which hold bounding box
        ImmutableArray<Entity> gamestate = TheEngine.getInstance().getEntitiesFor(Families.gamestateFamily);

        //TODO: Create ghost player, play a game with it, evaluate results
        double initTime = timer.getTime();

        // while we did not exceed the timelimit, play the game
        while (timer.getTime() - initTime <= timelimit) {
            TheEngine.getInstance().update((float) timer.getDeltaTime());
        }

        // clear engine
        TheEngine.getInstance().removeAllEntities();

        // reinitialize engine
        for (Entity entity : gamestate) {
            TheEngine.getInstance().addEntity(entity);
        }

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
