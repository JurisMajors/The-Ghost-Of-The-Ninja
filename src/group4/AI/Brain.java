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
    /** gamestate decoder to translate to a double array **/
    NNGameStateInterface decoder;

    /**
     * Engine stuff to an actual network
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
        // pass on the level decoder too
        this.decoder = b.decoder;
    }

    Brain () {
        this(Evolver.layerSizes, 1);
    }

    Brain (String filePath) throws IOException {
        File f = new File(filePath);
        nn = ModelSerializer.restoreMultiLayerNetwork(f);
    }

    void toFile(String filePath) throws IOException {
        ModelSerializer.writeModel(this.nn, filePath, false);
    }

    void setDecoder(NNGameStateInterface dec) {
        this.decoder = dec;
    }

    @Override
    protected Brain clone() {
        // clone the network
        MultiLayerNetwork networkClone = nn.clone();
        // put it in a new object
        Brain brainClone = new Brain(networkClone);
        // pass on the level decoder too
        brainClone.decoder = this.decoder;
        return brainClone;
    }

    /**
     * Feed forward the game state through the brain to get a move
     *
     * @param input decoded state of the game
     * @return move to make
     */
    public String feedForward(INDArray input) {
        // this can be changed that feed forward takes
        //  a game state as an input and does the decoding here
        // probably preferable, but no game state class yet
        //TODO: Use better move encoding
        List<INDArray> result = nn.feedForward(input);
        //TODO: Translate resulting array to a move
        return "";
    }
}
