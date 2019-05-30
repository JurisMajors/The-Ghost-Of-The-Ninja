package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import java.util.HashSet;
import java.util.Set;

public class DamageComponent implements Component {

    public int damage;
    public Set<Class<? extends Entity>> excluded;

    public DamageComponent(int damage) {
        this.damage = damage;
        excluded = new HashSet<>();
    }

    /**
     * @param damage
     * @param excluded list of entities which get skipped in the damage process
     */
    public DamageComponent(int damage, Set<Class<? extends Entity>> excluded) {
        this.damage = damage;
        this.excluded = excluded;
    }

}
