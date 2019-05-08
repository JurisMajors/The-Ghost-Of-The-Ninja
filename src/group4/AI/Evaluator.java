package group4.AI;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

/**
 * Evaluates individuals of the population
 */
public class Evaluator implements FitnessEvaluator<Brain> {

    @Override
    public double getFitness(Brain brain, List<? extends Brain> list) {
        //TODO: Create ghost player, play a game with it, evaluate results
        return 0;
    }

    /**
     * True if high fitness is better
     */
    @Override
    public boolean isNatural() {
        return false;
    }
}
