package group4.AI;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;
import org.uncommons.watchmaker.framework.operators.DoubleArrayCrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InefficientCrossover extends AbstractBrainCrossover {
    /**
     * watchmakers implementation of crossover
     * that is used after subdividing the NN array
     */
    AbstractCrossover helper;

    public InefficientCrossover() {
        super(1);
    }

    public InefficientCrossover(int crossoverPoints) {
        super(crossoverPoints);
        helper = new DoubleArrayCrossover(crossoverPoints);
    }

    public InefficientCrossover(int crossoverPoints, Probability crossoverProbability) {
        super(crossoverPoints, crossoverProbability);
        helper = new DoubleArrayCrossover(crossoverPoints, crossoverProbability);
    }

    public InefficientCrossover(NumberGenerator<Integer> crossoverPointsVariable) {
        super(crossoverPointsVariable);
        helper = new DoubleArrayCrossover(crossoverPointsVariable);
    }

    public InefficientCrossover(NumberGenerator<Integer> crossoverPointsVariable, NumberGenerator<Probability> crossoverProbabilityVariable) {
        super(crossoverPointsVariable, crossoverProbabilityVariable);
        helper = new DoubleArrayCrossover(crossoverPointsVariable, crossoverProbabilityVariable);
    }

    @Override
    protected List<List<INDArray>> crossover(INDArray par1W, INDArray par2W, Random rng) {
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
