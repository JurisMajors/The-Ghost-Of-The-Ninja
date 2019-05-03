package group4.AI;

/**
 * Does evolution of ghosts.
 * Takes two command line arguments: #generations, sizeOfPopulation
 */
public class StandardEvolver implements EvolverInterface {

    StandardEvolver() {}

    @Override
    public void initPopulation() {

    }

    @Override
    public void evolve(int nrGenerations, int popSize) {

    }

    @Override
    public void saveToFile() {

    }

    public static void main(String[] args) {
        EvolverInterface e = new StandardEvolver();
        int generations = Integer.parseInt(args[0]);
        int popSize = Integer.parseInt(args[1]);
        e.evolve(generations, popSize);
    }
}
