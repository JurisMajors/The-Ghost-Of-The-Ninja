package group4.AI;

import group4.AI.decoders.CircleVisionStateDecoder;
import group4.AI.decoders.ConeVisionStateDecoder;
import group4.AI.decoders.StateDecoderInterface;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Contains the neural network which acts as the "ghost" brain.
 */
public class Brain {
    /** the network that does the calculations **/
    MultiLayerNetwork nn;
    /** state decoder for this brain, by default: the current training decoder **/
    private StateDecoderInterface decoder = Evolver.decoder;

    /**
     * Given information about the layers, initialize a MLP
     */
    Brain (int[] layerSizes, int seed) {
        // TODO: FINALIZE NETWORK ARCHITECTURE

        NeuralNetConfiguration.ListBuilder lb = new NeuralNetConfiguration.Builder()
                .weightInit(WeightInit.XAVIER)
                .list();

        // build the dense layers
        for (int i = 0; i < layerSizes.length - 2; i++) {
            lb.layer(new DenseLayer.Builder().nIn(layerSizes[i]).nOut(layerSizes[i + 1])
                    .activation(Activation.RELU)
                    .weightInit(WeightInit.XAVIER)
                    .build());

        }
        // finalize the configuration by adding the output layer
        MultiLayerConfiguration conf = lb.layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .activation(Activation.SOFTMAX)
                .weightInit(WeightInit.XAVIER)
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
        this.decoder = b.decoder;
    }

    Brain () {
        this(Evolver.layerSizes, 1);
    }

    /**
     * Loads a network and its settings from files
     * @param modelPath model file defined by deeplearning4j (the neural net)
     * @param settings json file of settings of the brain, decoder etc.
     */
    public Brain (String modelPath, String settings) {
        try {
            FileReader fileReader = new FileReader(settings);
            // json object containing all necessary information about the brain
            JSONObject brainInfo = new JSONObject(new JSONTokener(fileReader));
            File f = new File(modelPath); // get path to model path
            // load the network model of the brain
            this.nn = ModelSerializer.restoreMultiLayerNetwork(f);
            // load the state decoder
            this.setDecoder(brainInfo.getJSONObject("decoder"));

        } catch (IOException e) {
            System.err.println("IOException was thrown with path " + settings + " or " + modelPath);
        }
    }

    void toFile(String filePath) throws IOException {
        ModelSerializer.writeModel(this.nn, filePath, false);
    }

    private void setDecoder(JSONObject info) throws IllegalStateException {
        String decoderClass = info.getString("name"); // get the name of the decoder
        // distinguish all possibilities and initialize according to the info
        if (decoderClass.endsWith("CircleVisionStateDecoder")) {
            int angleGap = info.getInt("gap");
            int nrRays = info.getInt("rays");
            this.decoder = new CircleVisionStateDecoder(nrRays, angleGap);
        } else if (decoderClass.endsWith("ConeVisionStateDecoder")) {
            int angleRange = info.getInt("range");
            int nrRays = info.getInt("rays");
            this.decoder = new ConeVisionStateDecoder(nrRays, angleRange);
        } else {
            throw new IllegalStateException("Trying to load a unrecognized state decoder " + decoderClass);
        }
    }

    /**
     * Feed forward the game state through the brain to get a move
     *
     * @param input decoded state of the game
     * @return move to make
     */
    private int feedForward(INDArray input) {
        List<INDArray> output = nn.feedForward(input);
        double[] result = output.get(output.size() - 1).toDoubleVector();
        int argMax = 0;
        // get best move defined by network
        for (int i = 1; i < result.length; i++) {
            if (result[i] > result[argMax]) {
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
        INDArray input = this.decoder.decode();
        return this.feedForward(input);
    }
}
