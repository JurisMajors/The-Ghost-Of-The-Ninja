package group4.AI.evaluation;

import group4.maths.Vector3f;

public class EuclideanStrategy extends AbstractEvaluationStrategy {
    @Override
    public float evaluate(Vector3f pos, Vector3f startPos, Vector3f goalPos) {
        return pos.euclidDist(goalPos) / startPos.euclidDist(goalPos);
    }

    @Override
    public boolean isNatural() {
        return false;
    }
}
