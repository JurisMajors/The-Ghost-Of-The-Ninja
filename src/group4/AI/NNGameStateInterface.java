package group4.AI;

/**
 * This acts as a decoder of the game states.
 * Translating it in to a form such that the NN can understand it. (double array)
 */
public interface NNGameStateInterface {
    /**
     * Translates GameState information in to a double array.
     * Used for neural network input.
     * @return array of doubles, size is #tiles on the screen
     */
    double[] decode(); //TODO: GameState);

    /**
     * Size of the decoded array
     * decode(AnyGameState).length == getInputSize()
     */
    int getInputSize();
}
