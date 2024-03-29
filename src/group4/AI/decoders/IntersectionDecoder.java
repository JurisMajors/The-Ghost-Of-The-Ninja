package group4.AI.decoders;

import com.badlogic.ashley.core.Entity;
import group4.maths.IntersectionPair;
import group4.maths.Ray;

/**
 * Interface providing features of an {@link IntersectionPair}
 */
public interface IntersectionDecoder {
    /**
     * Get the features of an intersection pair
     *
     * @param pair  the intersection pair to get the features from
     * @param ghost the ghost
     * @return the features as a float array
     */
    float[] getFeatures(Ray r, IntersectionPair pair, Entity ghost);

    /**
     * Number of features per pair this decoder produces
     */
    int nrFeatures();
}
