package group4.AI;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

public class BrainFactory extends AbstractCandidateFactory {
    int[] layerInfo;
    /**
     * Give neural network sizes.
     */
    BrainFactory(int[] sizes, NNGameStateInterface decoder) {
        layerInfo = new int[sizes.length + 2]; // input and output extra
        int input = decoder.getInputSize();

        // input layer
        layerInfo[0] =  input;
        // given layers
        for (int i = 1; i <= sizes.length ; i++) {
            layerInfo[i] = sizes[i - 1];
        }
        // output
        layerInfo[layerInfo.length - 1] = 4;

    }

    @Override
    public Object generateRandomCandidate(Random random) {
        return null;
    }
}
