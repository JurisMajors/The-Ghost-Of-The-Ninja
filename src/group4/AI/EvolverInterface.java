package group4.AI;

public interface EvolverInterface {

    /**
     * Initializes necessary data structures for storing the population and
     * the population itself.
     */
    void initPopulation(int size);

    /**
     * Does the evolution of the previously initialized population
     * @pre population was initialized
     * @param nrGenerations how many generations to evolve for
     * @param popSize how big the population should be
     */
    void evolve(int nrGenerations, int popSize);

    /**
     * Save the necessary information to a file.
     * For example, best performing network weights,
     * fitness functions
     */
    void saveToFile();

    /**
     * Log information about the evolution.
     */
    void log();

}
