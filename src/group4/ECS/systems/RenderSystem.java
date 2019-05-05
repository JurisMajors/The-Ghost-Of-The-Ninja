package group4.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.etc.Families;

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    public RenderSystem() {}

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Families.graphicsFamily);
    }

    public void removedFromEngine(Engine engine) {}

    public void update(float deltaTime) {}

    public boolean checkProcessing() { return false; }

    public void setProcessing(boolean processing) {}

}
