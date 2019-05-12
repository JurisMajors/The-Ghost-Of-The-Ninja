package group4.ECS;

import group4.maths.Vector3f;

/**
 * This class is a container for all the limitations that we put on the player
 * and consecutively on the ghost too.
 * It is intended to simply contain static variables which the player uses on initialization
 * and when it is manipulated through the ECS systems
 */
public final class PlayerRules {
    /** along with the position describes a cuboid with lbbCorner=position-dimention and rtfCorner=position+dimention **/
    public final static Vector3f dimension = new Vector3f(2.0f, 2.0f, 0.0f);
    /** Velocity when the player is initialized **/
    public final static Vector3f startVelocity = new Vector3f(0.0f, 0.0f, 0f);
    /** restricting the velocity: -velocityRange.x<=velocity.x<=velocityRange.x and velocity.y<=velocityRange.y **/
    public final static Vector3f velocityRange = new Vector3f(2.0f, 2.0f, 0.0f);
    /** the acceleration of the player **/
    public final static Vector3f acceleration = new Vector3f();
    /**  the gravity force on the player **/
    public final static Vector3f gravity = new Vector3f(0.0f, -9.81f, 0.0f);
}
