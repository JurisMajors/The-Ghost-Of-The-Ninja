package group4.AI.decoders;

import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * This acts as a decoder of the game states.
 * Translating it in to a form such that the NN can understand it. (INDArray)
 */
public interface StateDecoderInterface {
    /**
     * Translates GameState information in to a double array.
     * Used for neural network input.
     *
     * @return input for the neural net in INDAray format
     */
    INDArray decode();

    /**
     * Size of the decoded array
     * decode(AnyGameState).length == getInputSize()
     */
    int getInputSize();

    /**
     * Write the settings of this decoder to a json object
     *
     * @return json object representing the settings of the decoder instance
     */
    JSONObject getSettings();
}
