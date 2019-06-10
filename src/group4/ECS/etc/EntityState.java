package group4.ECS.etc;

public enum EntityState {
    NONE, // Like a null
    DEFAULT, // Like a default animation
    PLAYER_IDLE,
    PLAYER_WALKING,
    PLAYER_RUNNING,
    PLAYER_PREJUMP,
    PLAYER_JUMPING,
    PLAYER_FALLING,
    PLAYER_POSTFALL
}
