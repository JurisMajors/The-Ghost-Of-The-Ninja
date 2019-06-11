package group4.AI.evaluation;

import group4.maths.Vector3f;

public class XCoordStrategy extends SingleCoordStrategy {
    @Override
    public float evaluate(Vector3f pos, Vector3f startPos, Vector3f goalPos) {
        return super.coordDiff(pos.x, goalPos.x) / super.coordDiff(startPos.x, goalPos.x);
    }

    @Override
    public boolean isNatural() {
        return false;
    }
}
