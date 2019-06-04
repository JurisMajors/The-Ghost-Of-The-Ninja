package group4.AI.evaluation;

import group4.maths.Vector3f;

/**
 * Applying strategy pattern for evaluating how good is the location
 * relative to the goal position.
 * If the strategy is natural, then a bigger evaluation is better.
 * It is preferable that the evaluation is in the bounds [0,1] (at least roughly)
 *
 * It is very important that this strategy always produces a non-negative evaluation, otherwise the training
 * will stop.
 */
public abstract class AbstractEvaluationStrategy {
    /**
     * Evaluate a position, relative to the goal position
     * @param pos the position to evaluate
     * @param goalPos the goal position
     * @param startPos the starting position
     * @return the evaluation of pos
     */
    public abstract float evaluate(Vector3f pos, Vector3f startPos, Vector3f goalPos);

    /**
     * Whether this strategy is natural
     * @return true if bigger evaluation is better
     */
    public abstract boolean isNatural();

    /**
     * Get a strategy based on a string encoding
     * @param name name of the strategy
     * @return strategy linked to string name
     */
    public static AbstractEvaluationStrategy of(String name) throws IllegalArgumentException {
        if (name.equals("euclid") || name.endsWith("EuclideanStrategy")) {
            return new EuclideanStrategy();
        } else if (name.equals("ycoord") || name.endsWith("YCoordStrategy")) {
            return new YCoordStrategy();
        } else if (name.equals("xcoord") || name.endsWith("XCoordStrategy")) {
            return new XCoordStrategy();
        } else if (name.equals("manh") || name.endsWith("ManhattanStrategy")) {
            return new ManhattanStrategy();
        } else {
            throw new IllegalArgumentException("Trying to load unrecognized strategy by name " + name);
        }
    }
}
