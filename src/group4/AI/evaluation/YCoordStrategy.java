package group4.AI.evaluation;

import group4.maths.Vector3f;

public class YCoordStrategy extends SingleCoordStrategy {
    @Override
    public float evaluate(Vector3f pos, Vector3f startPos, Vector3f goalPos) {
        return super.coordDiff(pos.y, goalPos.y) / super.coordDiff(startPos.y, goalPos.y);
    }

    @Override
    public boolean isNatural() {
        return false;
    }
}
