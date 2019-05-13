package group4.AI.decoders;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.PositionComponent;
import group4.maths.IntersectionPair;
import group4.maths.Ray;
import group4.maths.Vector3f;

public class RayCastDecoder {
    /**
     * range of the angle that rays are cast in
     **/
    private float angleRange;
    /**
     * number of rays within that are casted
     **/
    private int nrRays;
    /**
     * intersection pair decoder for determining the features per intersection pair
     **/
    private IntersectionDecoder decoder;

    RayCastDecoder(float angleRange, int n, IntersectionDecoder id) {
        this.angleRange = angleRange;
        this.nrRays = n;
        this.decoder = id;
    }

    /**
     * Produces a double array which contains both the current ghost features and the casted ray features
     * which are determined according to the {@link IntersectionDecoder}.
     * Rays are casted in the positive direction.
     *
     * @param ghostFeatures already present features of the ghost itself
     * @param start         the starting direction of ray casting
     * @param entities      the entities to determine collisions with
     * @return the produced features from casting rays + ghost features at the start
     */
    double[] rayFeatures(double[] ghostFeatures, Vector3f start, Entity ghost, ImmutableArray<Entity> entities) {
        Vector3f ghostPos = ghost.getComponent(PositionComponent.class).position; // position of the ghost
        // define the length of the features array
        double[] features = new double[ghostFeatures.length + this.nrRays * decoder.nrFeatures()];
        // copy the ghost features
        for (int i = 0; i < ghostFeatures.length; i++) {
            features[i] = ghostFeatures[i];
        }
        // increments of the angle for the rays
        float deltaTheta = this.angleRange / this.nrRays;

        for (int i = ghostFeatures.length; i < features.length ; i++) {
            // create ray with appropriate direction
            // by rotating upwards from the start ray
            Ray r = new Ray(ghostPos, start.rotateXY((i - ghostFeatures.length) * deltaTheta));
            // cast it
            IntersectionPair intersection = r.cast(entities);

            // add the features to the result
            double[] interFeatures = this.decoder.getFeatures(intersection, ghost);
            for (int j = 0; j < interFeatures.length; j++) {
                features[j + i] = interFeatures[j];
            }

        }
        return features;
    }


}