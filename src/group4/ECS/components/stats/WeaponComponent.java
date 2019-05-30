package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;

public class WeaponComponent implements Component {

    public int damage;
    public float rateOfAttack;

    /* cooldown of weapon in seconds */
    public float cooldown;

    /**
     * @param damage
     * @param rateOfAttack hits per second, see e.g. {@link group4.ECS.systems.combat.PlayerCombatSystem}
     */
    public WeaponComponent(int damage, float rateOfAttack) {
        this.damage = damage;
        this.rateOfAttack = rateOfAttack;
        this.cooldown = 0.0f;
    }

}
