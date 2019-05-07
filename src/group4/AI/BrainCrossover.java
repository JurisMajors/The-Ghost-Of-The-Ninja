package group4.AI;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;
import org.uncommons.watchmaker.framework.operators.DoubleArrayCrossover;

import java.util.*;

public class BrainCrossover extends AbstractCrossover<Brain> {
    /**
     * watchmakers implementation of crossover
     * that is used after subdividing the NN array
     */
    AbstractCrossover helper;

    public BrainCrossover() {
        this(1);
    }

    public BrainCrossover(int crossoverPoints) {
        super(crossoverPoints);
        helper = new DoubleArrayCrossover(crossoverPoints);
    }

    public BrainCrossover(int crossoverPoints, Probability crossoverProbability) {
        super(crossoverPoints, crossoverProbability);
        helper = new DoubleArrayCrossover(crossoverPoints, crossoverProbability);
    }

    public BrainCrossover(NumberGenerator<Integer> crossoverPointsVariable) {
        super(crossoverPointsVariable);
        helper = new DoubleArrayCrossover(crossoverPointsVariable);
    }

    public BrainCrossover(NumberGenerator<Integer> crossoverPointsVariable, NumberGenerator<Probability> crossoverProbabilityVariable) {
        super(crossoverPointsVariable, crossoverProbabilityVariable);
        helper = new DoubleArrayCrossover(crossoverPointsVariable, crossoverProbabilityVariable);
    }

    @Override
    protected List<Brain> mate(Brain parent1, Brain parent2, int i, Random random) {

        // get parent iterators over the weights
        List<Brain> offsprings = new ArrayList<>();
        Iterator p1Witer = parent1.nn.paramTable().entrySet().iterator();
        Iterator p2Witer = parent2.nn.paramTable().entrySet().iterator();

        // get model architectures from the parents
        Brain offspring1 = new Brain(parent1);
        Brain offspring2 = new Brain(parent1);

        // get the parameter tables from the offspring, that we modify later
        Map<String, INDArray> o1Params = offspring1.nn.paramTable();
        Map<String, INDArray> o2Params = offspring2.nn.paramTable();

        // https://stackoverflow.com/questions/42806761/initialize-custom-weights-in-deeplearning4j

        while (p1Witer.hasNext() && p2Witer.hasNext()) { // should be the same, but for safety
            Map.Entry<String, INDArray> p1Entry = (Map.Entry<String, INDArray>) p1Witer.next();
            Map.Entry<String, INDArray> p2Entry = (Map.Entry<String, INDArray>) p2Witer.next();

            // keys should be same for both
            String layer = p1Entry.getKey();
            // don't crossover bias
            if (layer.endsWith("b")) continue;

            // get the weights of the layer
            INDArray p1W = p1Entry.getValue();
            INDArray p2W = p2Entry.getValue();
            // get new weights
            List<List<INDArray>> offW = crossover(p1W, p2W, random);

            // add the new weights to the offspring
            assignWeights(offW.get(0), layer, o1Params);
            assignWeights(offW.get(1), layer, o2Params);
        }

        offsprings.add(offspring1);
        offsprings.add(offspring2);
        return offsprings;
    }

    private void assignWeights(List<INDArray> W, String key, Map<String, INDArray> params) {
        for (int i = 0; i < W.size(); i++) {
            params.get(key).putRow(i, W.get(i));
        }
    }

    private List<List<INDArray>> crossover(INDArray par1W, INDArray par2W, Random rng) {
        List<List<INDArray>> newWeights = new ArrayList<>(); // contains new weights for both offsprings
        List<INDArray> firstOffWeights = new ArrayList<>(); // offspring1 weights
        List<INDArray> scndOffWeights = new ArrayList<>(); // offspring2 weights

        newWeights.add(firstOffWeights);
        newWeights.add(scndOffWeights);

        long rowIndex = 0;
        long nRows = par1W.shape()[0]; // [nRows, nCols]

        while (rowIndex < nRows) {
            // TODO: Implement this part with INDarrays instead of using helper for efficiency
            double[] p1Wd = par1W.getRow(rowIndex).toDoubleVector();
            double[] p2Wd = par2W.getRow(rowIndex).toDoubleVector();
            List<double[]> helperParents = new ArrayList<>();
            helperParents.add(p1Wd);
            helperParents.add(p2Wd);
            // crossover these double arrays
            List<double[]> offspring = helper.apply(helperParents, rng);
            // create INDarrays from doubles
            firstOffWeights.add(Nd4j.create(offspring.get(0)));
            scndOffWeights.add(Nd4j.create(offspring.get(1)));

            rowIndex++;
        }
        return newWeights;
    }
}

