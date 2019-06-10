package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import java.util.HashSet;
import java.util.Set;

public class DamageComponent implements Component {

    public int damage;
    public Entity origin;
    public Set<Class<? extends Entity>> excluded;

    /**
     * default constructor
     * @param damage damage the entity is dealing
     */
    public DamageComponent(int damage, Entity origin) {
        this.damage = damage;
        this.origin = origin;
        excluded = new HashSet<>();
    }

    /**
     * @param damage damage the entity is dealing
     * @param excluded list of entities which get skipped in the damage process
     */
    public DamageComponent(int damage, Set<Class<? extends Entity>> excluded, Entity origin) {
        this.damage = damage;
        this.origin = origin;
        this.excluded = excluded;
    }

}
