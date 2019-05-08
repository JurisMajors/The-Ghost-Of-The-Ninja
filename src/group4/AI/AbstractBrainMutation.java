package group4.AI;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.*;

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

    Brain apply(Brain b, Random r) {

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
            INDArray w = entry.getValue();
            // mutate the weight
            mutateWeight(w, r);
            mutatedB.nn.setParam(key, w);
        }

        return mutatedB;
    }

    /**
     * Mutates the weight according to the rng
     * @param w weight to attempt to mutate
     * @param rng  number generator, for determining whether to mutate
     * @modifies w possibly mutating some elements in it according to mutationProb
     */
    protected abstract void mutateWeight(INDArray w, Random rng);


    @Override
    public List<Brain> apply(List<Brain> list, Random random) {
        // watchmakers framework specifies that new objects have to be created
        // not modify the given
        List<Brain> mutated = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mutated.add(apply(list.get(i), random));
        }
        return mutated;
    }

}
