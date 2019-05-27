package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class DamageComponent implements Component {

    public int damage;
    public Entity origin;

    public DamageComponent(int damage) {
        this.damage = damage;
        origin = null;
    }

    public DamageComponent(int damage, Entity dmgOrigin) {
        this.damage = damage;
        this.origin = dmgOrigin;
    }

}
