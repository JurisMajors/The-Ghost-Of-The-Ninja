package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;
import group4.ECS.entities.items.Item;

public class PlayerComponent implements Component{

    // inventory of size 8, let Item[0] be the active item
    public Item[] inventory;

    public PlayerComponent() {
        inventory = new Item[8];
    }

}
