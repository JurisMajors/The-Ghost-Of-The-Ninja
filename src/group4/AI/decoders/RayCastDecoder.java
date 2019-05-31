package group4.AI.decoders;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.identities.ExitComponent;
import group4.ECS.components.identities.GhostComponent;
import group4.ECS.components.identities.PlayerComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.maths.IntersectionPair;
import group4.maths.Ray;
import group4.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;

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
    float[] rayFeatures(float[] ghostFeatures, Vector3f start, Entity ghost, ImmutableArray<Entity> entities) {
        Vector3f ghostCenter = ghost.getComponent(PositionComponent.class).position.add(
                ghost.getComponent(DimensionComponent.class).dimension.scale(0.5f)); // position of the ghost

        // define the length of the features array
        float[] features = new float[ghostFeatures.length + this.nrRays * decoder.nrFeatures()];
        // copy the ghost features
        for (int i = 0; i < ghostFeatures.length; i++) {
            features[i] = ghostFeatures[i];
        }
        // increments of the angle for the rays
        float deltaTheta = this.angleRange / this.nrRays;
        List<Class<? extends Component>> ignorables = new ArrayList<>();
        ignorables.add(GhostComponent.class);
        ignorables.add(ExitComponent.class);
        ignorables.add(PlayerComponent.class);

        for (int i = ghostFeatures.length; i < this.nrRays ; i+=2) {
            // create ray with appropriate direction
            // by rotating upwards from the start ray
            Ray r = new Ray(ghostCenter, start.rotateXY((i - ghostFeatures.length) * deltaTheta), ignorables);
            // cast it
            IntersectionPair intersection = r.cast(entities);

            // add the features to the result
            float[] interFeatures = this.decoder.getFeatures(r, intersection, ghost);
            for (int j = 0; j < interFeatures.length; j++) {
                features[j + i] = interFeatures[j];
            }
        }
        return features;
    }


}
