package group4.AI;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.MovementComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.TheEngine;
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
        Vector3f ghostVel = ghost.getComponent(MovementComponent.class).velocity; // the velocity of the ghost
        // bottom of the cone of vision
        Vector3f bot = ghostVel.rotateXY(-1 * angleRange / 2);

        double[] result = new double[this.getInputSize()];
        float deltaTheta = angleRange / this.nrRays;

        for (int i = 0; i < this.nrRays; i++) {
            // create ray
            Ray r = new Ray(ghostPos, bot.rotateXY(i * deltaTheta));
            // cast it
            Vector3f intersection = r.cast(entities);
            // add it to the result
            result[i] = intersection.euclidDist(ghostPos);
        }
        INDArray indResult = Nd4j.create(result);
        return indResult;
    }

    @Override
    public int getInputSize() {
        return 2 * this.nrRays;
    }
}

