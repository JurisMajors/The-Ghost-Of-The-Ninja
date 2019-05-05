package group4.AI;

import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.List;
import java.util.Random;

public class BrainCrossover extends AbstractCrossover<Brain> {

    public BrainCrossover() {
        this(1);
    }

    public BrainCrossover(int crossoverPoints) {
        super(crossoverPoints);
    }

    public BrainCrossover(int crossoverPoints, Probability crossoverProbability) {
        super(crossoverPoints, crossoverProbability);
    }

    public BrainCrossover(NumberGenerator<Integer> crossoverPointsVariable) {
        super(crossoverPointsVariable);
    }

    public BrainCrossover(NumberGenerator<Integer> crossoverPointsVariable, NumberGenerator<Probability> crossoverProbabilityVariable) {
        super(crossoverPointsVariable, crossoverProbabilityVariable);
    }

    @Override
    protected List<Brain> mate(Brain brain, Brain t1, int i, Random random) {
        // below a link to see how to modify d4j weights
        // https://stackoverflow.com/questions/42806761/initialize-custom-weights-in-deeplearning4j
        return null;
    }
}
