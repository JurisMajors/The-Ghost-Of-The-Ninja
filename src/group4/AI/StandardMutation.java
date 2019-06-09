package group4.AI;

import org.nd4j.linalg.api.iter.NdIndexIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;

import java.util.Random;

/**
 * Applies a very basic mutation rule to the weight array.
 * According to the given probability on initialization of the class it
 * replaces a single element of the weight matrix by  a random double.
 */
public class StandardMutation extends AbstractBrainMutation{

    public StandardMutation(Probability prob) {
        super(new ConstantGenerator(prob));
    }

    public StandardMutation(NumberGenerator<Probability> mutationProb) {
        super(mutationProb);
    }

    public StandardMutation() {
        super(new ConstantGenerator(new Probability(0.02)));
    }

    @Override
    protected void mutateWeight(INDArray w, Random rng) {
        // INDArray iterator
        NdIndexIterator iter = new NdIndexIterator(w.shape());
        while (iter.hasNext()) { // while there are values to iterate
            long[] nextIndex = iter.next(); // get index
            if ((this.mutationProb.nextValue()).nextEvent(rng)) { // if should mutate by the probability
                float r = rng.nextFloat();
                w = w.putScalar(nextIndex, r); // generate new double for the weight
            }
        }

    }
}
