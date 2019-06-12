package group4.ECS.systems.GraphHandlers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.AStarMobComponent;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;

public class AStarMobGraphSystem extends IteratingSystem {

    public AStarMobGraphSystem(int priority) {
        super(Families.aStarMobFamily, priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        AStarMobComponent aStarMobComponent = Mappers.aStarMobMapper.get(entity);

        aStarMobComponent.handler.constructGraph(entity);
    }
}
