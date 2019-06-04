package group4.AI.evaluation;

public abstract class SingleCoordStrategy extends AbstractEvaluationStrategy {
    protected float coordDiff(float coord1, float coord2) {
        return Math.abs(coord2 - coord1);
    }
}
