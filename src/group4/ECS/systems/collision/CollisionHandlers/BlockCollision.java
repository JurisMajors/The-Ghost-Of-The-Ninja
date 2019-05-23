package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.world.Block;

public class BlockCollision extends AbstractCollisionHandler<Block> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new BlockCollision();

    @Override
    public void collision(Block e, CollisionComponent cc) {

    }

    public static AbstractCollisionHandler getInstance() {
        return me;
    }
}
