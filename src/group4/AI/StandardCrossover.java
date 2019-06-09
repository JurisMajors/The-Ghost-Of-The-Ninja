package group4.AI;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.nd4j.linalg.indexing.NDArrayIndex.interval;

/**
 * Same crossover rule as {@link InefficientCrossover} however,
 * not using the watchmakers helper and directly working with INDArrays.
 */
public class StandardCrossover extends AbstractBrainCrossover {

    public StandardCrossover() {
        super(1);
    }

    public StandardCrossover(int crossoverPoints) {
        super(crossoverPoints);
    }

    public StandardCrossover(int crossoverPoints, Probability crossoverProbability) {
        super(crossoverPoints, crossoverProbability);
    }

    public StandardCrossover(NumberGenerator<Integer> crossoverPointsVariable) {
        super(crossoverPointsVariable);
    }

    public StandardCrossover(NumberGenerator<Integer> crossoverPointsVariable, NumberGenerator<Probability> crossoverProbabilityVariable) {
        super(crossoverPointsVariable, crossoverProbabilityVariable);
    }

    @Override
    protected List<List<INDArray>> crossover(INDArray par1W, INDArray par2W, Random rng) {
        List<List<INDArray>> newWeights = new ArrayList<>(); // contains new weights for both offsprings
        List<INDArray> firstOffWeights = new ArrayList<>(); // offspring1 weights
        List<INDArray> scndOffWeights = new ArrayList<>(); // offspring2 weights

        newWeights.add(firstOffWeights);
        newWeights.add(scndOffWeights);

        long rowIndex = 0;
        long[] shape = par1W.shape();
        long nRows = shape[0]; // [nRows, nCols]
        long nCols = shape[1];

        while (rowIndex < nRows) {
            // get current rows
            INDArray p1WRow = par1W.getRow(rowIndex);
            INDArray p2WRow = par2W.getRow(rowIndex);
            // get random index to crossover over
            long crossoverIndex = (1 + rng.nextInt((int) nCols - 1));
            // get parts of the parent rows
            INDArray p1First = p1WRow.get(NDArrayIndex.all(), interval(0, crossoverIndex));
            INDArray p2Scnd = p2WRow.get(NDArrayIndex.all(), interval(crossoverIndex, nCols));
            // concatenate them to one offspring
            firstOffWeights.add(Nd4j.concat(1, p1First, p2Scnd));

            INDArray p1Scnd = p1WRow.get(NDArrayIndex.all(), interval(crossoverIndex, nCols));
            INDArray p2First = p2WRow.get(NDArrayIndex.all(), interval(0, crossoverIndex));
            scndOffWeights.add(Nd4j.concat(1, p2First, p1Scnd));

            rowIndex++;
        }
        return newWeights;
    }
}
