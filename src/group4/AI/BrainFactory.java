package group4.AI;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Arrays;
import java.util.Random;

/**
 * Creates a random brain
 */
public class BrainFactory extends AbstractCandidateFactory<Brain> {
    int[] layerInfo;
    /**
     * Give neural network sizes.
     */
    BrainFactory(int[] sizes) {
        layerInfo = new int[sizes.length + 2]; // input and output extra
        int input = Evolver.decoder.getInputSize();

        // input layer
        layerInfo[0] =  input;
        // given layers
        for (int i = 1; i <= sizes.length ; i++) {
            layerInfo[i] = sizes[i - 1];
        }
        // output
        layerInfo[layerInfo.length - 1] = 3;

    }

    @Override
    public Brain generateRandomCandidate(Random random) {
        Brain b = new Brain(layerInfo, random.nextInt());
        return b;
    }
}
