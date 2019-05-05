package group4.AI;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;

public class BrainMutation<Brain> implements EvolutionaryOperator<Brain> {
    @Override
    public List<Brain> apply(List<Brain> list, Random random) {
        // below to see how to modify networks weights in d4j
        // https://stackoverflow.com/questions/42806761/initialize-custom-weights-in-deeplearning4j
        return null;
    }
}
