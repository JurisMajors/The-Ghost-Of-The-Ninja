package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.PlayerComponent;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.components.stats.ScoreComponent;
import group4.ECS.systems.collision.CollisionHandlers.PlayerCollision;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;


public class Player extends Entity {

    /**
     * dimension of player aka bounding box, ghost inherits in order to apply texture
     */
    protected Vector3f dimension = new Vector3f(1.0f, 1.5f, 0.0f);

    /**
     * the level that the player is part of
     */
    public Level level;

    /**
     * Acceleration of the player. E.g., when sprinting
     */
    protected Vector3f accel = new Vector3f(0.01f, 0.0f, 0.0f);

    /**
     * The absolute range of velocity the player can get in each direction
     */
    protected Vector3f velocityRange = new Vector3f(0.2f, 0.25f, 0.0f);

    /**
     * The part of velocityRange, that is used for velocity, when not sprinting
     */
    public static final float walkingRatio = 3.0f/4.0f;

    public boolean spawnedGhost;

    /**
     * -1 if not on a totem
     * else the id of the totem player is on
     */
    public int totemStatus;

    /**
     * Creates a player
     *
     * @param position center point of player
     * @param level the level that the player is part of
     */
    public Player(Vector3f position, Level level) {
        totemStatus = -1;

        // shader
        Shader shader = Shader.SIMPLE;
        // TODO: proper texture
        Texture texture = Texture.PLAYER;

        // Level
        this.level = level;
        this.spawnedGhost = false;

        // add needed components
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        // temporary!!, player should initially not move
        this.add(new MovementComponent(new Vector3f(), velocityRange, accel));
        this.add(new GravityComponent());
        this.add(new GraphicsComponent(shader, texture, dimension, false));
        this.add(new HealthComponent(100));
        this.add(new PlayerComponent());
        this.add(new CollisionComponent(PlayerCollision.getInstance()));
        this.add(new ScoreComponent());
    }

    public static String getName() {
        return "Player";
    }
}
