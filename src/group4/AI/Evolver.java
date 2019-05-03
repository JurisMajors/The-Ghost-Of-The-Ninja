package group4.AI;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Does the process of evolving and outputs the best fitting network.
 * Three arguments- popSize, elitism, generationCount
 * popSize = size of the population within a generation
 * elitism = ...
 * generationCount = how many generations until termination
 */
public class Evolver {

    public static void main(String[] args) {
        List<EvolutionaryOperator<Brain>> operators = new LinkedList<>();
        operators.add(new BrainCrossover());
        operators.add(new BrainMutation());

        EvolutionaryOperator<Brain> pipeline = new EvolutionPipeline<>(operators);

        AbstractCandidateFactory<Brain> factory = new BrainFactory();
        FitnessEvaluator<Brain> fitnessEvaluator = new Evaluator<>();
        SelectionStrategy<Object> selection = new RouletteWheelSelection();
        Random rng = new MersenneTwisterRNG();

        EvolutionEngine<Brain> engine = new GenerationalEvolutionEngine<>(factory, pipeline,
                fitnessEvaluator, selection, rng);

        int popSize = Integer.parseInt(args[0]);
        int elitism = Integer.parseInt(args[1]);
        int genCount = Integer.parseInt(args[2]);

        Brain result = engine.evolve(popSize, elitism, new GenerationCount(genCount));
    }
}
