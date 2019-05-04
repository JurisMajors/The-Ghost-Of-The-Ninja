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
 *
 * command-line arguments - filePath
 * filePath = where to store best model
 */
public class Evolver {
    public final static int populationSize = 100;
    public final static int elitism = (int)(populationSize * 0.2);
    public final static int genCount = 100;
    public final static int maxFit = Integer.MAX_VALUE;


    private static void toFile(Brain b, String filePath) throws IOException {
        b.toFile(filePath);
    }

    private static int[] getLayerSizes(String arg) {
        return null;
    }

    public static void main(String[] args) {
        String path = args[0];

        List<EvolutionaryOperator<Brain>> operators = new LinkedList<>();
        operators.add(new BrainCrossover());
        operators.add(new BrainMutation());

        // put them in a pipeline
        EvolutionaryOperator<Brain> pipeline = new EvolutionPipeline<>(operators);

        // factory of neural networks
        AbstractCandidateFactory<Brain> factory = new BrainFactory();
        FitnessEvaluator<Brain> fitnessEvaluator = new Evaluator<>();
        SelectionStrategy<Object> selection = new RouletteWheelSelection();
        Random rng = new MersenneTwisterRNG();

        EvolutionEngine<Brain> engine = new GenerationalEvolutionEngine<>(factory, pipeline,
                fitnessEvaluator, selection, rng);


        // uncomment to run

        Brain result = engine.evolve(Evolver.populationSize, Evolver.elitism,
                new GenerationCount(Evolver.genCount),
                new TargetFitness(Evolver.maxFit, true));

        // uncomment to save resulting weights
        // Evolver.toFile(result, path);
    }
}
