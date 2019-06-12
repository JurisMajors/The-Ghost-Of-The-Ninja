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
import group4.ECS.etc.EntityState;
import group4.ECS.components.stats.ScoreComponent;
import group4.ECS.entities.totems.StartTotem;
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
    public static final float walkingRatio = 3.0f / 4.0f;

    /**
     * Whether the player has spawned a ghost and it is alive
     */
    public boolean spawnedGhost;
    public boolean challanging = false;
    public int startTotemID = -1;

    /**
     * The starting totem that the player is touching
     * Null if not touching
     */
    public StartTotem totemStatus;
    /*
     * Variable to keep track of state of entity
     */
    protected EntityState state = EntityState.PLAYER_IDLE;

    /**
     * Creates a player
     *
     * @param position center point of player
     * @param level    the level that the player is part of
     */
    public Player(Vector3f position, Level level) {
        totemStatus = null;

        // shader
        Shader shader = Shader.SIMPLE;
        // TODO: proper texture
        Texture texture = Texture.DEBUG;

        // Level
        this.level = level;
        this.spawnedGhost = false;

        // add needed components
        this.add(new PositionComponent(new Vector3f(position)));
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

    /**
     * Set the state of the entity
     *
     * @param s The new state of the entity
     */
    public void setState(EntityState s) {
        if (this.state != s) {
            this.state = s;
//            System.out.println(s); // Uncomment for state change logging
        }
    }

    /**
     * Get the current state of the entity
     */
    public EntityState getState() {
        return this.state;
    }
}
