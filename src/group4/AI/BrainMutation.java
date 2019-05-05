package group4.AI;

import org.nd4j.linalg.api.iter.NdIndexIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.*;

public class BrainMutation implements EvolutionaryOperator {
    private final NumberGenerator<Probability> mutationProb;


    public BrainMutation(Probability prob) {
        this(new ConstantGenerator(prob));
    }

    public BrainMutation(NumberGenerator<Probability> mutationProb) {
        this.mutationProb = mutationProb;
    }

    public BrainMutation() {
        this(new ConstantGenerator(new Probability(0.02)));
    }

    Brain apply(Brain b, Random r) {
        // below to see how to modify networks weights in d4j
        // https://stackoverflow.com/questions/42806761/initialize-custom-weights-in-deeplearning4j

        // clone the brain
        Brain mutatedB = new Brain(b);
        // apply mutation rule
        Iterator weightIter = mutatedB.nn.paramTable().entrySet().iterator(); // get iterator over weights of the neural net

        while (weightIter.hasNext()) { // loop through the weights
            Map.Entry<String, INDArray> entry = (Map.Entry<String, INDArray>) weightIter.next();
            String key = entry.getKey(); // info about the matrix, ends with W if a weight matrix
            if (key.endsWith("W")) { // if not bias
                INDArray w = entry.getValue();
                // mutate the weight
                mutateWeight(w, r);
                mutatedB.nn.setParam(key, w);
            }
        }

        return mutatedB;
    }

    /**
     * Mutates the weight according to the rng
     */
    private void mutateWeight(INDArray w, Random rng) {
        // INDArray iterator
        NdIndexIterator iter = new NdIndexIterator(w.shape());
        while (iter.hasNext()) { // while there are values to iterate
            long[] nextIndex = iter.next(); // get index
            if ((this.mutationProb.nextValue()).nextEvent(rng)) { // if should mutate by the probability
                w.putScalar(nextIndex, rng.nextDouble()); // generate new double for the weight
            }
        }
    }


    @Override
    public List apply(List list, Random random) {
        // watchmakers framework specifies that new objects have to be created
        // not modify the given
        List<Brain> mutated = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mutated.add(apply((Brain) list.get(i), random));
        }
        return mutated;
    }

}
