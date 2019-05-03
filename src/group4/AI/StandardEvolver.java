package group4.AI;

import java.io.IOException;
import java.util.logging.*;

/**
 * Does evolution of ghosts.
 * Takes three command line arguments: logpath, #generations, sizeOfPopulation
 * logpath must include the file to write to.
 */
public class StandardEvolver implements EvolverInterface {
    Logger logger;
    FileHandler fh;

    StandardEvolver(String logpath, boolean logToConsole) {
        logger = Logger.getLogger("StandardEvolver");

        try {
            fh = new FileHandler(logpath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.addHandler(fh);
        logger.setUseParentHandlers(logToConsole);

        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        // init logs
        logger.info(logger.getName() + " successfuly initialized");
    }

    @Override
    public void initPopulation(int size) {
        logger.info("Initializing population of size " + size);
        logger.info("Population initialized!");
    }

    @Override
    public void evolve(int nrGenerations, int popSize) {
        initPopulation(popSize);
        logger.info("Starting evolution for " + nrGenerations + " generations.");

    }

    @Override
    public void saveToFile() {

    }

    @Override
    public void log() {

    }

    public static void main(String[] args) {
        EvolverInterface e = new StandardEvolver(args[0], false);

        int generations = Integer.parseInt(args[1]);
        int popSize = Integer.parseInt(args[2]);

        e.evolve(generations, popSize);
    }
}
