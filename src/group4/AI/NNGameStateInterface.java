package group4.AI;

public interface NNGameStateInterface {
    /**
     * Translates GameState information in to a double array.
     * Used for neural network input.
     * @return array of doubles, size is #tiles on the screen
     */
    double[] decode(); //TODO: GameState);
}
