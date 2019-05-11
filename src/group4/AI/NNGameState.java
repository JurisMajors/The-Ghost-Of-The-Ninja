package group4.AI;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.TheEngine;
import group4.maths.IntersectionPair;
import group4.maths.Ray;
import group4.maths.Vector3f;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class NNGameState implements NNGameStateInterface {

    /** nr of rays to cast **/
    private int nrRays;
    /** range of the ray casting angle **/
    private float angleRange = 160.0f;

    NNGameState(int n) {
        this.nrRays = n;
    }

    @Override
    public INDArray decode() {
        // get all the entities
        ImmutableArray<Entity> entities = TheEngine.getInstance().getEntities();
        // get only the ghost
        Entity ghost = TheEngine.getInstance().getEntitiesFor(Families.ghostFamily).get(0);

        Vector3f ghostPos = ghost.getComponent(PositionComponent.class).position; // position of the ghost
        // normalized velocity of the ghost
        Vector3f ghostVel = ghost.getComponent(MovementComponent.class).velocity.normalized();
        // bottom of the cone of vision
        Vector3f bot = ghostVel.rotateXY(-1 * angleRange / 2);

        double[] result = new double[this.getInputSize()];
        float deltaTheta = angleRange / this.nrRays;
        // give velocity of the ghost
        result[0] = ghostVel.x;
        result[1] = ghostVel.y;
        // give intersection info
        for (int i = ghostFeatureAmount(); i < this.nrRays; i++) {
            // create ray with appropriate direction
            // by rotating upwards from the bottom ray
            Ray r = new Ray(ghostPos, bot.rotateXY((i - ghostFeatureAmount()) * deltaTheta));
            // cast it
            IntersectionPair intersection = r.cast(entities);
            Vector3f interPoint = intersection.point;
            Entity interEntity = intersection.entity;
            // add it to the result
            result[i] = interPoint.euclidDist(ghostPos);
            result[i + 1] = decodeEntity(interEntity);
        }
        return Nd4j.create(result);
    }

    @Override
    public int getInputSize() {
        return 2 * this.nrRays + this.ghostFeatureAmount();
    }

    /**
     * # of features that are given to the ghost as input
     */
    private int ghostFeatureAmount() {
        return 2;
    }

    private int decodeEntity(Entity e) {
        return 0;
    }
}

