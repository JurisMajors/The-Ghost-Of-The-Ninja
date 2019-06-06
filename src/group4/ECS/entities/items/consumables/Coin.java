package group4.ECS.entities.items.consumables;

import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.CoinComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.items.Item;
import group4.ECS.systems.collision.CollisionHandlers.CoinCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

public class Coin extends Item {

    public static final Vector3f SMALL_SIZE = new Vector3f(0.75f, 0.75f, 0);
    public static final Vector3f LARGE_SIZE = new Vector3f(1f, 1f, 0);

    public static final int SMALL_VALUE = 50;
    public static final int LARGE_VALUE = 100;

    /**
     * Creates a coin with score value value.
     *
     * @param position  bottom left position of the coin
     * @param dimension size of the coin
     * @param shader    shader
     * @param texture   (tilemap) texture
     * @param texCoords coorinates on tilemap
     * @param value     score value of the coin
     */
    public Coin(Vector3f position, Vector3f dimension, Shader shader, Texture texture, float[] texCoords, int value) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new CoinComponent(value));
        this.add(new CollisionComponent(CoinCollision.getInstance()));
        this.add(new GraphicsComponent(shader, texture, dimension, texCoords, false));
    }

    public static String getName() {
        return "Coin";
    }
}