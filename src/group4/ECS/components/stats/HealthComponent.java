package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import group4.ECS.etc.EntityConst;

import java.util.HashSet;
import java.util.Set;

public class HealthComponent implements Component {

    public int health;
    public Set<EntityConst.EntityState> state;

    /**
     * holds both information about health and gameplay related states
     * TODO: make effects overlap, i.e. don't use Hashset
     *
     * @param health health of the entity
     */
    public HealthComponent (int health) {
        this.health = health;
        this.state = new HashSet<>();
    }

}
