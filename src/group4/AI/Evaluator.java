package group4.AI;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

public class Evaluator<Brain> implements FitnessEvaluator<Brain> {

    @Override
    public double getFitness(Brain brain, List<? extends Brain> list) {
        //TODO: Create ghost player, play a game with it, evaluate results
        return 0;
    }

    @Override
    public boolean isNatural() {
        return false; // if true then high fitness score better
                        // if false then low fitness better
    }
}
