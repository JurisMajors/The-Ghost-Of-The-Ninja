package group4.AI;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.*;

public abstract class AbstractBrainCrossover extends AbstractCrossover<Brain> {

    public AbstractBrainCrossover() {
        this(1);
    }

    public AbstractBrainCrossover(int crossoverPoints) {
        super(crossoverPoints);
    }

    public AbstractBrainCrossover(int crossoverPoints, Probability crossoverProbability) {
        super(crossoverPoints, crossoverProbability);
    }

    public AbstractBrainCrossover(NumberGenerator<Integer> crossoverPointsVariable) {
        super(crossoverPointsVariable);
    }

    public AbstractBrainCrossover(NumberGenerator<Integer> crossoverPointsVariable, NumberGenerator<Probability> crossoverProbabilityVariable) {
        super(crossoverPointsVariable, crossoverProbabilityVariable);
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

            if (offW == null || offW.get(0) == null
                    || offW.get(1) == null) throw new NullPointerException("Offspring weights are null!");

            if (offW.size() != 2) throw new IllegalStateException("Produced " + offW.size() + " offspring, instead of 2");

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

    /**
     * Apply crossover rule on two parent weights
     * @param par1W weights of first parent
     * @param par2W weights of the second parent
     * @param rng random number generator
     * @return List of lists, which first list contains weights for the first offspring and the second list weights for first offspring
     * @post \return.size() == 2 && \return != null && \return.get(0) != null && \return.get(1) != null
     */
    protected abstract List<List<INDArray>> crossover(INDArray par1W, INDArray par2W, Random rng);

}

