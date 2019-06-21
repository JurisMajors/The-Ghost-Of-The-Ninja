package group4.ECS.entities.items.consumables;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.ConsumableComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.etc.EntityConst;
import group4.ECS.systems.collision.CollisionHandlers.PotionCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class HealthPotion extends Entity {

    public HealthPotion(Vector3f position) {

        Vector3f dimension = new Vector3f(0.5f, 0.5f, 0.0f);
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new CollisionComponent(PotionCollision.getInstance()));
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.POTION, dimension, false));
        this.add(new ConsumableComponent(EntityConst.ItemID.HEALTH_POTION, EntityConst.EffectID.SMALL_HEAL));

    }

    public static String getName() {
        return "HealthPotion";
    }

}
