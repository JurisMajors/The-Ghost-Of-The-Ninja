package group4.AI;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.IOException;
import java.io.File;

/**
 * Contains the neural network which acts as the "ghost" brain.
 */
public class Brain {
    MultiLayerNetwork nn;
    /**
     * Engine stuff to an actual network
     */
    Brain (int[] layerSizes) {
        //TODO: given some parameters translate to a feed forward NN
    }

    Brain () {
        //TODO: can do some custom network here
    }

    Brain (String filePath) throws IOException {
       //TODO: read weights from the filepath
        File f = new File(filePath);
        nn = ModelSerializer.restoreMultiLayerNetwork(f);
    }

    void toFile(String filePath) throws IOException {
        ModelSerializer.writeModel(this.nn, filePath, false);
    }

    /**
     * Feed forward the game state
     *
     * @param state the current state of the game
     * @return move to make
     */
    public String feedForward(NNGameStateInterface state) {
        //TODO: Use better move encoding
        return "";
    }
}
