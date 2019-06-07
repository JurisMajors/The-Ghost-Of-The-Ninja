package group4.ECS.systems.collision.CollisionHandlers;

import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.entities.Totem;
import group4.ECS.systems.collision.CollisionData;

public class TotemCollision extends AbstractCollisionHandler<Totem> {
    private static AbstractCollisionHandler me;

    @Override
    public void collision(Totem e, CollisionComponent cc) {
        boolean collideWPlayer = false;
        for (CollisionData cd : cc.collisions) {
            if (cd.entity instanceof Player && !(cd.entity instanceof Ghost)) {
                collideWPlayer = true;
            }
        }
        e.level.getPlayer().onTotem = collideWPlayer;
        cc.collisions.clear();
    }

    public static AbstractCollisionHandler getInstance() {
        return TotemCollision.me;
    }
}
