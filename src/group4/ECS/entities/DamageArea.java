package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.etc.TheEngine;
import group4.maths.Vector3f;

public abstract class DamageArea extends Entity {

    /**
     *
     * @param position position of the damage area
     * @param dimension dimension field of effect
     * @param damage damage inflicted on colliding entity (HealthComponent needed)
     */
    public DamageArea(Vector3f position, Vector3f dimension, int damage) {

        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new DamageComponent(damage));

        // add entity to engine on construction
        TheEngine.getInstance().addEntity(this);

    }

}
