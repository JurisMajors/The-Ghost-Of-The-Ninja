package group4.AI;

public interface EvolverInterface {

    /**
     * Initializes necessary data structures for storing the population and
     * the population itself.
     */
    void initPopulation();

    /**
     * Does the evolution of the previously initialized population
     * @pre population was initialized
     * @param nrGenerations how many generations to evolve for
     * @param popSize how big the population should be
     */
    void evolve(int nrGenerations, int popSize);

    void saveToFile();

}
