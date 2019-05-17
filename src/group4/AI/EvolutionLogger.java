package group4.AI;

import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;

import java.io.IOException;

/**
 * Logs the evolutionary process.
 */
public class EvolutionLogger implements EvolutionObserver<Brain> {
    /** every genToSave best individual is saved **/
    private int genToSave;
    /** directory where to save the best individuals **/
    String filePath;

    EvolutionLogger(String filePath, int genToSave) {
        this.genToSave = genToSave;
        this.filePath = filePath;
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
        System.out.printf("Current generation: %d \n Mean Fitness: %f \n Best Fitness: %f \n STD fitness : %f",
                data.getGenerationNumber(), data.getMeanFitness(),
                data.getBestCandidateFitness(), data.getFitnessStandardDeviation());

        // save best candidate every number of generations
        if (this.filePath != null &&
                data.getGenerationNumber() % genToSave == 0
                && data.getGenerationNumber() != 0) {

            Brain best = data.getBestCandidate();
            try {
                // save it to file with some information in file name and timestamp
                best.toFile(this.filePath + "Gen-" + data.getGenerationNumber() +
                        "-Fit-" + data.getBestCandidateFitness() + "-" + System.currentTimeMillis());
            } catch (IOException e) {
                System.err.println("WARNING: Individuals are not being saved because " +
                                    "IOException was thrown on path " + this.filePath);
            }
        }
    }
}
