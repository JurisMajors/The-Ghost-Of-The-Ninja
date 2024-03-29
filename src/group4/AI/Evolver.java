package group4.AI;

import group4.AI.decoders.CircleVisionStateDecoder;
import group4.AI.decoders.StateDecoderInterface;
import group4.AI.evaluation.AbstractEvaluationStrategy;
import group4.AI.evaluation.EuclideanStrategy;
import group4.AI.evaluation.Evaluator;
import org.apache.commons.cli.*;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.StochasticUniversalSampling;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Does the process of evolving and outputs the best fitting network.
 */
public class Evolver {
    /**
     * nr of brains per generation
     **/
    public static int populationSize = 50;
    /**
     * How many fittest individuals to keep over generations
     **/
    public static int elitism = (int) (populationSize * 0.2);
    /**
     * generation count until termination
     **/
    public static int genCount = 50;
    /**
     * if max fit known, then terminate on that otherwise leave max val.
     **/
    public static int maxFit = 0;
    /**
     * hidden layer sizes (dont include input/output)
     **/
    public static int[] layerSizes = new int[]{100, 100};
    /**
     * Number of rays to cast
     */
    public static int rays = 60;
    /**
     * decoder of gamestates
     **/
    public static StateDecoderInterface decoder = new CircleVisionStateDecoder(Evolver.rays, 10, 10);
    /**
     * probability to completely change a weight of nn
     **/
    public static double mutationProbability = 0.1;
    /**
     * Mutator
     **/
    private static AbstractBrainMutation mutator = new StandardMutation(new Probability(mutationProbability));
    /**
     * Crossover
     **/
    private static AbstractBrainCrossover crossover = new StandardCrossover();
    /**
     * Evaluation strategy
     */
    static AbstractEvaluationStrategy evaluationStrat = new EuclideanStrategy();
    /**
     * time limit for the module to train, after this time has reached, the ghost is killed
     **/
    public static double timelimit = 5.00;
    /**
     * Path to save trained models
     **/
    public static String path = "src/group4/AI/models/";
    /**
     * Path for loading the module to train on
     **/
    public static String modulePath = "./src/group4/res/maps/level_01/modules/10.json";
    /**
     * How often (in generations) to save a model
     **/
    public static int checkpoint = 2;

    /**
     * Whether to render the training process
     */
    public static boolean render = false;
    public static boolean multiThreaded = true;

    private static OptionGroup AIoptions = new OptionGroup();

    /**
     * Whether to save the json settings of brains only once during the training
     * (Useful if dont want to pollute the folder and if all brains have same settings)
     */
    public static boolean saveSettingsOnce = true;

    private static void toFile(Brain b, String filePath) throws IOException {
        b.toFile(filePath, !saveSettingsOnce);
    }

    public static void parseArgs(CommandLine cmd) {
        Evolver.setGivenOptions(cmd);
    }

    public static void train() {
        List<EvolutionaryOperator<Brain>> operators = new LinkedList<>();
        operators.add(mutator);
        operators.add(crossover);

        // put them in a pipeline
        EvolutionaryOperator<Brain> pipeline = new EvolutionPipeline<>(operators);

        // factory of neural networks
        AbstractCandidateFactory<Brain> factory = new BrainFactory(Evolver.layerSizes);
        FitnessEvaluator<Brain> fitnessEvaluator = new Evaluator(Evolver.evaluationStrat);
        SelectionStrategy<Object> selection = new StochasticUniversalSampling();
        Random rng = new MersenneTwisterRNG();

        AbstractEvolutionEngine<Brain> engine = new GenerationalEvolutionEngine<>(factory, pipeline,
                fitnessEvaluator, selection, rng);

        engine.setSingleThreaded(!multiThreaded);

        System.err.println("-------------------------------------------");
        EvolutionLogger logger = new EvolutionLogger(path, checkpoint);
        System.err.println("-------------------------------------------");
        System.err.println();
        System.err.println("Starting genetic evolution on module at location " + Evolver.modulePath);
        System.err.println("Saving logs (and models) at " + Evolver.path);
        if (Evolver.multiThreaded) {
            System.err.println("Multi threading is enabled, pay attention to CPU temps!");
        } else if (Evolver.render) {
            System.err.println("Rendering the training process! (do nogui flag to disable rendering)");
        }
        System.err.println();

        // add logger to the engine
        engine.addEvolutionObserver(logger);
        Brain result = engine.evolve(Evolver.populationSize, Evolver.elitism,
                new GenerationCount(Evolver.genCount),
                new TargetFitness(Evolver.maxFit, false));
        try {
            Evolver.toFile(result, path + "BEST-" + System.currentTimeMillis());
        } catch (IOException e) {
            System.err.println("Warning: Could not save final model on path:\n" + path);
        }
    }


    public static OptionGroup addOptions() {
        Option module = new Option("m", "module", true, "Training module json, default: " + Evolver.modulePath);
        module.setRequired(false); // can use default path within source code
        AIoptions.addOption(module);

        Option logPath = new Option("l", "log", true, "Path to logs for txt logs and model checkpoints, default: " + Evolver.path);
        logPath.setRequired(false);
        AIoptions.addOption(logPath);

        Option popSize = new Option("p", "pop", true, "Population size, default: 50");
        popSize.setRequired(false);
        AIoptions.addOption(popSize);

        Option gen = new Option("g", "generations", true, "Nr of generations, default: 50");
        gen.setRequired(false);
        AIoptions.addOption(gen);

        Option timelimit = new Option("t", "timelimit", true, "Timelimit for the ghosts (in seconds), default: 5s");
        timelimit.setRequired(false);
        AIoptions.addOption(timelimit);

        Option mutationProb = new Option("mutation", true, "Mutation probability in interval [0,1], default: 0.1");
        mutationProb.setRequired(false);
        AIoptions.addOption(mutationProb);

        Option rays = new Option("rays", true, "Number of rays, default:60");
        rays.setRequired(false);
        AIoptions.addOption(rays);

        Option layers = new Option("layers", true, "commma seperated network layer sizes (not including input, output). Default value 100,100");
        layers.setRequired(false);
        AIoptions.addOption(layers);

        Option check = new Option("checkpoint", true, "how often, in generations, to save models, default:2");
        check.setRequired(false);
        AIoptions.addOption(check);

        Option settings = new Option("s", "settings", false, "activate this flag if " +
                "you want to save settings of models on each checkpoint, otherwise they are simply saved once");
        check.setRequired(false);
        AIoptions.addOption(settings);

        Option evalStrat = new Option("eval", true, "Choose your evaluation strategy, " +
                "default:euclid. [euclid, xcoord, ycoord, manh]");
        check.setRequired(false);
        AIoptions.addOption(evalStrat);

        Option render = new Option("nogui",false, "Use this flag if you want to multi-thread training. Disables visualization");
        check.setRequired(false);
        AIoptions.addOption(render);

        return AIoptions;
    }

    private static void setGivenOptions(CommandLine cmd) {
        if (cmd.hasOption("module")) {
            Evolver.modulePath = cmd.getOptionValue("module");
        }
        if (cmd.hasOption("log")) {
            Evolver.path = cmd.getOptionValue("log");
        }
        if (cmd.hasOption("pop")) {
            Evolver.populationSize = Integer.parseInt(cmd.getOptionValue("pop"));
        }
        if (cmd.hasOption("generations")) {
            Evolver.genCount = Integer.parseInt(cmd.getOptionValue("generations"));
        }
        if (cmd.hasOption("timelimit")) {
            Evolver.timelimit = Float.parseFloat(cmd.getOptionValue("timelimit"));
        }
        if (cmd.hasOption("mutation")) {
            Evolver.mutationProbability = Float.parseFloat(cmd.getOptionValue("mutation"));
            mutator = new StandardMutation(new Probability(Evolver.mutationProbability));
        }
        if (cmd.hasOption("rays")) {
            Evolver.rays = Integer.parseInt(cmd.getOptionValue("rays"));
            Evolver.decoder = new CircleVisionStateDecoder(Evolver.rays, 10, 10);
        }
        if (cmd.hasOption("layers")) {
            String[] sizes = cmd.getOptionValue("layers").split(",");
            Evolver.layerSizes = new int[sizes.length];
            for (int i = 0; i < sizes.length; i++) {
                Evolver.layerSizes[i] = Integer.parseInt(sizes[i]);
            }
        }
        if (cmd.hasOption("checkpoint")) {
            Evolver.checkpoint = Integer.parseInt(cmd.getOptionValue("checkpoint"));
        }
        if (cmd.hasOption("settings")) {
            Evolver.saveSettingsOnce = false;
        }
        if (cmd.hasOption("eval")) {
            Evolver.evaluationStrat = AbstractEvaluationStrategy.of(cmd.getOptionValue("eval"));
        }
        if (!cmd.hasOption("nogui")) {
            Evolver.render = true;
            Evolver.multiThreaded = false;
        }
    }
}
