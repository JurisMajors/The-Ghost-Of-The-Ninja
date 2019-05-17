package group4.AI;

import group4.AI.decoders.CircleVisionStateDecoder;
import group4.AI.decoders.StateDecoderInterface;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
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
 */
public class Evolver {
    /**
     * nr of brains per generation
     **/
    public final static int populationSize = 50;
    /**
     * How many fittest individuals to keep over generations
     **/
    public final static int elitism = (int) (populationSize * 0.2);
    /**
     * generation count until termination
     **/
    public final static int genCount = 50;
    /**
     * if max fit known, then terminate on that otherwise leave max val.
     **/
    public final static int maxFit = Integer.MAX_VALUE;
    /**
     * hidden layer sizes (dont include input/output)
     **/
    public final static int[] layerSizes = new int[]{100};
    /**
     * decoder of gamestates
     **/
    public final static StateDecoderInterface decoder = new CircleVisionStateDecoder(60, 10);
    /**
     * probability to completely change a weight of nn
     **/
    public final static double mutationProbability = 0.1;
    /**
     * Mutator
     **/
    private static AbstractBrainMutation mutator = new StandardMutation(new Probability(mutationProbability));
    /**
     * Crossover
     **/
    private static AbstractBrainCrossover crossover = new StandardCrossover();
    /** TODO: find feasible time limit **/
    public static double timelimit = 5.00;
    /** Model path **/
    public static final String path = "src/group4/AI/models/";
    /**
     * whether to render
     */
    public static final boolean render = true;
    /**
     * Currently not supported for GA
     */
    public static final boolean multiThreaded = false;

    private static void toFile(Brain b, String filePath) throws IOException {
        b.toFile(filePath);
    }

    public static void aiSht() {

        List<EvolutionaryOperator<Brain>> operators = new LinkedList<>();
        operators.add(mutator);
        operators.add(crossover);

        // put them in a pipeline
        EvolutionaryOperator<Brain> pipeline = new EvolutionPipeline<>(operators);

        // factory of neural networks
        AbstractCandidateFactory<Brain> factory = new BrainFactory(Evolver.layerSizes);
        FitnessEvaluator<Brain> fitnessEvaluator = new Evaluator();
        SelectionStrategy<Object> selection = new RouletteWheelSelection();
        Random rng = new MersenneTwisterRNG();

        AbstractEvolutionEngine<Brain> engine = new GenerationalEvolutionEngine<>(factory, pipeline,
                fitnessEvaluator, selection, rng);

        engine.setSingleThreaded(!multiThreaded);

        EvolutionLogger logger = new EvolutionLogger(path, 2);

        // add logger to the engine
        engine.addEvolutionObserver(logger);

        // uncomment to run
        Brain result = engine.evolve(Evolver.populationSize, Evolver.elitism,
                new GenerationCount(Evolver.genCount),
                new TargetFitness(Evolver.maxFit, true));

        // uncomment to save resulting weights
        // Evolver.toFile(result + "BEST", path);
    }

}
