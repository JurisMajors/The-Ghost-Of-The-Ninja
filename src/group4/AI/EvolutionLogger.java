package group4.AI;

import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logs the evolutionary process.
 */
public class EvolutionLogger implements EvolutionObserver<Brain> {
    /** every genToSave best individual is saved **/
    private int genToSave;

    /** directory where to save the best individuals **/
    String filePath;

    /** best fitness in the whole training process **/
    double bestFitness = -1;

    private final static Logger LOGGER = Logger.getLogger("EvolutionLog"); // used for logging to file

    EvolutionLogger(String filePath, int genToSave) {
        this.genToSave = genToSave;
        this.filePath = filePath;
        // if null, then cant initialize logger
        if (this.filePath == null) return;

        try {
            FileHandler fh = new FileHandler(filePath + "evolution-log-" + System.currentTimeMillis()
                    + "-.log") ;
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            LOGGER.info("Logger initialized! \n" +
                    "Layers: " + Arrays.toString(Evolver.layerSizes) + "\n" +
                    "Population: " + Evolver.populationSize + "\n" +
                    "MutationProb: " + Evolver.mutationProbability + "\n" +
                    "Rays: " + Evolver.rays + "\n" +
                    "Evaluation: " + Evolver.evaluationStrat.getClass().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use this constructor if don't want to save intermediate best individuals
     */
    EvolutionLogger() {
        this (null, -1);
    }


    /**
     * Logs the current population and saves the best candidate to file
     *
     * @param data current data on the population
     */
    @Override
    public void populationUpdate(PopulationData<? extends Brain> data) {
        String log = String.format("Current generation: %d / %d \n Mean Fitness: %f \n Best Fitness: %f \n STD fitness: %f",
                data.getGenerationNumber(), Evolver.genCount, data.getMeanFitness(),
                data.getBestCandidateFitness(), data.getFitnessStandardDeviation());

        if (this.filePath != null) {
            LOGGER.info(log);
        }
        boolean forceSave = false;
        if (bestFitness < data.getBestCandidateFitness()) { // found better fitness than before
            bestFitness = data.getBestCandidateFitness(); // save it in the variable
            forceSave = true; // and save the model
        }

        // save best candidate every number of generations
        if ((this.filePath != null &&
                data.getGenerationNumber() % genToSave == 0)
                || forceSave) {

            Brain best = data.getBestCandidate();
            try {
                String modelName = this.filePath + "Gen-" + data.getGenerationNumber() +
                        "-Fit-" + data.getBestCandidateFitness() + "-" + System.currentTimeMillis();
                // save it to file with some information in file name and timestamp
                best.toFile(modelName, !(Evolver.saveSettingsOnce && data.getGenerationNumber() > genToSave));
            } catch (IOException e) {
                System.err.println("WARNING: Individuals are not being saved because " +
                                    "IOException was thrown on path " + this.filePath);
            }
        }
    }
}
