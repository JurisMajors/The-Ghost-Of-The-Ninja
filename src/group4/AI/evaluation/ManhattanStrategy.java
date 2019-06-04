package group4.AI.evaluation;

import group4.maths.Vector3f;

public class ManhattanStrategy extends AbstractEvaluationStrategy {
    @Override
    public float evaluate(Vector3f pos, Vector3f startPos, Vector3f goalPos) {
        return pos.manhDist(goalPos) / startPos.manhDist(goalPos);
    }
    @Override
    public boolean isNatural() {
        return false;
    }
}
