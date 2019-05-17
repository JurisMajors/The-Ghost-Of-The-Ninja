        package group4.AI;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Contains the neural network which acts as the "ghost" brain.
 */
public class Brain {
    /** the network that does the calculations **/
    MultiLayerNetwork nn;

    /**
     * Given information about the layers, initialize a MLP
     */
    Brain (int[] layerSizes, int seed) {
        // TODO: FINALIZE NETWORK ARCHITECTURE

        NeuralNetConfiguration.ListBuilder lb = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .weightInit(WeightInit.XAVIER)
                .list();

        // build the dense layers
        for (int i = 0; i < layerSizes.length - 1; i++) {
            lb.layer(new DenseLayer.Builder().nIn(layerSizes[i]).nOut(layerSizes[i + 1])
                    .activation(Activation.RELU)
                    .build());

        }
        // finalize the configuration by adding the output layer
        MultiLayerConfiguration conf = lb.layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .activation(Activation.SOFTMAX)
                .nIn(layerSizes[layerSizes.length - 2]).nOut(layerSizes[layerSizes.length - 1]).build())
                .build();

        conf.setBackprop(false);
        conf.setPretrain(false);

        // apply the configuration
        nn = new MultiLayerNetwork(conf);
        nn.init();
    }

    Brain (MultiLayerNetwork network) {
        this.nn = network;
    }

    Brain (Brain b) {
         // clone the network
        this.nn = b.nn.clone();
    }

    Brain () {
        this(Evolver.layerSizes, 1);
    }

    public Brain (String filePath) {
        try {
            File f = new File(filePath);
            nn = ModelSerializer.restoreMultiLayerNetwork(f);
        } catch (IOException e) {
            System.err.println("IOException was thrown with path " + filePath);
        }
    }

    void toFile(String filePath) throws IOException {
        ModelSerializer.writeModel(this.nn, filePath, false);
    }

    /**
     * Feed forward the game state through the brain to get a move
     *
     * @param input decoded state of the game
     * @return move to make
     */
    private int feedForward(INDArray input) {
        double[] result = nn.output(input).toDoubleVector();
        int argMax = 0;
        // get best move defined by network
        for (int i = 1; i < result.length; i++) {
            if (result[i] > argMax) {
                argMax = i;
            }
        }
        return argMax;
    }

    /**
     * Calculates the move the ghost should take
     * @return the move the ghost should take, according to {@link GhostMove} enum
     */
    public int think() {
        return this.feedForward(Evolver.decoder.decode());
    }
}
