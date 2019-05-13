package group4.AI.decoders;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.MovementComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Decodes the game state by casting rays in a certain angle range.
 * For each ray, the features are determined by the {@link RayStateDecoder}.
 * And features for each intersection are determined by the pair {@link StandardIntersectionDecoder} decoder.
 */
public abstract class RayStateDecoder implements StateDecoderInterface {

    /**
     * nr of rays to cast
     **/
    private int nrRays;
    /**
     * range of the ray casting angle (in degrees)
     **/
    float angleRange;
    /**
     * The ray caster which combines the given feature factories in a double array
     */
    private RayCastDecoder rayDecoder;

    public RayStateDecoder(int n, float angleRange) {
        this.nrRays = n;
        this.angleRange = angleRange;
        this.rayDecoder = new RayCastDecoder(this.angleRange, this.nrRays, new StandardIntersectionDecoder());
    }

    @Override
    public INDArray decode() {
        // get all the entities
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntities();
        // get only the ghost
        Entity ghost = TheEngine.getInstance().getEntitiesFor(Families.ghostFamily).get(0);

        // normalized velocity of the ghost
        Vector3f ghostVel = ghost.getComponent(MovementComponent.class).velocity.normalized();
        // bottom of the cone of vision
        Vector3f bot = getCastingStart(ghost);

        double[] ghostFeatures = new double[this.ghostFeatureAmount()];
        // give velocity of the ghost (already normalized)
        ghostFeatures[0] = ghostVel.x;
        ghostFeatures[1] = ghostVel.y;
        double[] result = this.rayDecoder.rayFeatures(ghostFeatures, bot, ghost, entities);
        return Nd4j.create(result);
    }

    @Override
    public int getInputSize() {
        return 2 * this.nrRays + this.ghostFeatureAmount();
    }

    /**
     * # of features that are given to the ghost as input
     */
    protected int ghostFeatureAmount() {
        return 2;
    }

    /**
     * Determines the direction from which ray casting starts
     * @param ghost the current ghost
     * @return direction where the first ray will be cast
     */
    abstract Vector3f getCastingStart(Entity ghost);
}
