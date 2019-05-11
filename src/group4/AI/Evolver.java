package group4.AI;

import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.levels.SimpleLevel;
import group4.levelSystem.modules.SimpleModule;
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
 * command-line arguments - filePath
 * filePath = directory to store the best model
 */
public class Evolver {
    /** nr of brains per generation **/
    public final static int populationSize = 100;
    /** How many fittest individuals to keep over generations **/
    public final static int elitism = (int)(populationSize * 0.2);
    /** generation count until termination **/
    public final static int genCount = 100;
    /** if max fit known, then terminate on that otherwise leave max val. **/
    public final static int maxFit = Integer.MAX_VALUE;
    /** hidden layer sizes (dont include input/output) **/
    public final static int[] layerSizes = new int[]{100, 200, 300, 100};
    /** decoder of gamestates **/
    public final static NNGameStateInterface decoder = new NNGameState(50);
    /** probability to completely change a weight of nn **/
    public final static double mutationProbability = 0.05;
    /** Mutator **/
    private static AbstractBrainMutation mutator = new StandardMutation(new Probability(mutationProbability));
    /** Crossover **/
    private static AbstractBrainCrossover crossover = new StandardCrossover();
    /** the level (temporary) **/
    public static Level level = new SimpleLevel();
    /** TODO: find feasible time limit **/
    public static double timelimit = 120.00;

    private static void toFile(Brain b, String filePath) throws IOException {
        b.toFile(filePath);
    }

    public static void main(String[] args) {
        String path = args[0];

        List<EvolutionaryOperator<Brain>> operators = new LinkedList<>();
        operators.add(mutator);
        operators.add(crossover);

        // put them in a pipeline
        EvolutionaryOperator<Brain> pipeline = new EvolutionPipeline<>(operators);

        // factory of neural networks
        AbstractCandidateFactory<Brain> factory = new BrainFactory(Evolver.layerSizes, decoder);
        FitnessEvaluator<Brain> fitnessEvaluator = new Evaluator();
        SelectionStrategy<Object> selection = new RouletteWheelSelection();
        Random rng = new MersenneTwisterRNG();

        EvolutionEngine<Brain> engine = new GenerationalEvolutionEngine<>(factory, pipeline,
                fitnessEvaluator, selection, rng);

        EvolutionLogger logger = new EvolutionLogger(path, 10);

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
