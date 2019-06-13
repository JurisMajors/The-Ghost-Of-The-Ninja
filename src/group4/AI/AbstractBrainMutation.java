package group4.AI;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.*;

/**
 * Abstract class for mutating weights of a brain.
 * Mutating a weight matrice according to some mutating rule (specific implementation)
 */
public abstract class AbstractBrainMutation implements EvolutionaryOperator<Brain> {
    protected final NumberGenerator<Probability> mutationProb;


    public AbstractBrainMutation(Probability prob) {
        this(new ConstantGenerator(prob));
    }

    public AbstractBrainMutation(NumberGenerator<Probability> mutationProb) {
        this.mutationProb = mutationProb;
    }

    public AbstractBrainMutation() {
        this(new ConstantGenerator(new Probability(0.02)));
    }

    /**
     * Applies the mutation on a single brain
     *
     * @param b brain to potentially mutate
     * @param r rng for determening whether to mutate according to probability
     * @return a new brain with mutated weights
     */
    Brain apply(final Brain b, Random r) {

        // clone the brain
        Brain mutatedB = new Brain(b);
        // apply mutation rule
        Iterator weightIter = mutatedB.nn.paramTable().entrySet().iterator(); // get iterator over weights of the neural net

        // https://stackoverflow.com/questions/42806761/initialize-custom-weights-in-deeplearning4j
        while (weightIter.hasNext()) { // loop through the weights
            Map.Entry<String, INDArray> entry = (Map.Entry<String, INDArray>) weightIter.next();
            String key = entry.getKey(); // info about the matrix, ends with W if a weight matrix
            if (key.endsWith("b")) continue;
            // mutate the weight thats not bias
            INDArray w = entry.getValue().dup();
            // mutate the weight
            mutateWeight(w, r);
            mutatedB.nn.setParam(key, w);
        }

        return mutatedB;
    }

    /**
     * Mutates the weight according to the rng
     *
     * @param w   weight to attempt to mutate
     * @param rng number generator, for determining whether to mutate
     * @modifies w possibly mutating some elements in it according to mutationProb
     */
    protected abstract void mutateWeight(INDArray w, Random rng);

    /**
     * Apply the mutation on all given brains
     * watchmakers framework specifies that new objects have to be created without modifying the given
     *
     * @param list   list of brains
     * @param random rng for determining the probability to mutate
     * @return new list of brains that have the mutation rule applied to them
     */
    @Override
    public List<Brain> apply(final List<Brain> list, Random random) {
        List<Brain> mutated = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mutated.add(apply(list.get(i), random));
        }
        return mutated;
    }

}
