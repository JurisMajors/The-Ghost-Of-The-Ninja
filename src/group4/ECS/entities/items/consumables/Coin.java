package group4.ECS.entities.items.consumables;

import group4.ECS.components.identities.CoinComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.entities.items.Item;

public class Coin extends Item {

    /**
     * Creates a coin with score value value.
     * @param value score value
     */
    public Coin(int value) {
        this.add(new CoinComponent(value));
        this.add(new CollisionComponent());
    }

}
