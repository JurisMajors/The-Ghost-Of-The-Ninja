package group4.AI;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Does the process of evolving and outputs the best fitting network.
 * command-line arguments- popSize, elitism, generationCount, maxExpectedFitness, filePath
 * popSize = size of the population within a generation
 * elitism = ...
 * generationCount = how many generations until termination
 * maxExpectedFitness = -1 or the max achievable score. -1 == "I HAVE NO IDEA"
 * filePath = where to store best model
 */
public class Evolver {

    private static void toFile(Brain b, String filePath) throws IOException {
        b.toFile(filePath);
    }

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
        int tmpfit = Integer.parseInt(args[3]);
        String path = args[4];

        int maxFit = tmpfit == -1 ? Integer.MAX_VALUE : tmpfit;

        // uncomment to run

        Brain result = engine.evolve(popSize, elitism,
                new GenerationCount(genCount),
                new TargetFitness(maxFit, true));

        // uncomment to save resulting weights
        // Evolver.toFile(result, path);
    }
}
