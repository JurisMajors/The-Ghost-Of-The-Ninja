package group4.AI.decoders;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.MobComponent;
import group4.ECS.components.PositionComponent;
import group4.maths.IntersectionPair;
import group4.maths.Vector3f;

/**
 * Produces two features per intersection pair
 * First- the euclidean distance to the ghost
 * Second- the type of entity it intersects with as an integer (0 - mob, 1-otherwise)
 */
public class StandardIntersectionDecoder implements IntersectionDecoder {

    @Override
    public float[] getFeatures(IntersectionPair pair, Entity ghost) {
        float[] features = new float[this.nrFeatures()];
        // get the position
        Vector3f ghostPos = ghost.getComponent(PositionComponent.class).position;
        features[0] = pair.point.euclidDist(ghostPos);
        features[1] = this.decodeEntity(pair.entity);
        return features;
    }

    private float decodeEntity(Entity e) {
        if (e == null) return 0.5f;
        if (e.getComponent(MobComponent.class) != null) {
            return 0;
        }
        return 1;
    }

    @Override
    public int nrFeatures() {
        return 2;
    }
}
