package group4.ECS.components.stats;

public class WeaponComponent extends ItemComponent {

    public int damage;

    /**
     * @param damage
     * @param cooldown the cooldown to which the item is set after usage
     */
    public WeaponComponent(int damage, float cooldown) {
        super(cooldown);
        this.damage = damage;
    }

}
