package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import group4.ECS.entities.items.weapons.Spear;
import group4.ECS.entities.items.weapons.Sword;

public class PlayerComponent implements Component {

    // inventory of size 8, let Item[0] be the active item
    public Entity[] inventory;
    public Entity activeItem;

    public PlayerComponent() {
        this.inventory = new Entity[8];
        // temp!!!
        this.inventory[0] = new Sword();
        this.inventory[1] = new Spear();
        this.activeItem = inventory[0];
    }

}
