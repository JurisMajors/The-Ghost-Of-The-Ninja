package group4.ECS.systems.animation;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.AnimationComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.etc.EntityState;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;
import group4.maths.spline.Spline;
import group4.utils.DebugUtils;
import sun.security.ssl.Debug;

public class AnimationSystem extends IteratingSystem {
    float deltaTime;

    public AnimationSystem(int priority) {
        super(Families.animationFamily, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.deltaTime = deltaTime;

        if (entity instanceof HierarchicalPlayer) {
            animatePlayer((HierarchicalPlayer) entity);
        } else {
            // Assumption: player/ghost is a special case, the rest we (currently) handle via here.
            AnimationComponent ac = Mappers.animationMapper.get(entity);
            ac.getCurrentAnimation().update(this.deltaTime);
        }
    }

    private void animatePlayer(HierarchicalPlayer player) {
        AnimationComponent ac = Mappers.animationMapper.get(player);
        EntityState playerState = player.getState();

        // Update the active animation to match the current state of the player
        ac.setAnimation(player.getState());

        // Update the positions according to the animation that is currently active
        boolean done = ac.getCurrentAnimation().update(this.deltaTime);

        // Check if we have to handle a DelayedAnimation(Set) transition
        if (ac.getCurrentAnimation() instanceof DelayedAnimationSet && done) {
            if (playerState == EntityState.PLAYER_PREJUMP) {
                player.setState(EntityState.PLAYER_JUMPING);
            } else if (playerState == EntityState.PLAYER_POSTFALL) {
                player.setState(EntityState.PLAYER_IDLE);
            } else {
                System.err.println("[WARNING] Unhandled DelayedAnimation(Set) transition.");
            }
        }

        // Temp thing to animate the hip bounce during walking
        player.setHipPosition(player.getTorso().getComponent(PositionComponent.class).position.add(player.hipOffset));
        player.getTorso().relativePosition = player.getHipPosition();

        // Handle the IK logic for determining final limb rotations. Animations only update key positions, such as those of the IKEndEffector(s)
        for (String handleName : player.IKHandles.keySet()) {
            IKEndEffector handle = player.IKHandles.get(handleName);

            // Move handle.endPos to be relative of the player position
            PositionComponent pc = Mappers.positionMapper.get(handle);

            pc.position = pc.position.add(new Vector3f(player.getComponent(DimensionComponent.class).dimension.x / 2, 0.0f, 0.0f));

            // Get the length of the body parts involved in the IK system
            float upperLength = handle.upper.getComponent(DimensionComponent.class).dimension.y;
            float lowerLength = handle.lower.getComponent(DimensionComponent.class).dimension.y;

            // Calculate and set the new rotations for the limbs based on the start and endpoints of the IK chain
            // Check if we are dealing with a left or a right limb
            boolean left = handleName.endsWith("L");
            boolean bendForward = handleName.startsWith("foot");
            float[] angles = player.getLimbAngles(player.getOffsetHipPosition(left), pc.position, upperLength, lowerLength, bendForward);
            handle.upper.rotation = angles[0];
            handle.lower.rotation = angles[1];


            // Debug Drawings Of position
            Vector3f baseDebugPosition = pc.position.add(player.getComponent(PositionComponent.class).position);

            if (!bendForward)
                baseDebugPosition.addi(player.shoulderOffset);

            if (left || !bendForward) continue; // Don't  clutter screen

            DebugUtils.drawLine(baseDebugPosition.sub(new Vector3f(0.2f, 0.2f, 0.0f)), baseDebugPosition.add(new Vector3f(0.2f, 0.2f, 0.0f)));
            DebugUtils.drawLine(baseDebugPosition.sub(new Vector3f(0.2f, -0.2f, 0.0f)), baseDebugPosition.add(new Vector3f(0.2f, -0.2f, 0.0f)));
        }

        // Draw the spline for debug
        if (ac.getCurrentAnimation() instanceof AnimationSet) {
            for (Animation a : ((AnimationSet) ac.getCurrentAnimation()).animations) {
                if (a instanceof SplineAnimation) {
                    // Dont clutter screen
                    if (!(a.target instanceof IKEndEffector) || !((IKEndEffector) a.target).label.endsWith("R") || !((IKEndEffector) a.target).label.startsWith("hand")) {
                        continue;
                    }

                    SplineAnimation currentAnim = (SplineAnimation) a;
                    for (Spline s : currentAnim.getSpline().getSplines()) {
                        // Translate to right position
                        Vector3f[] sp = s.getPoints().clone();
                        for (int i = 0; i < sp.length; i++) {
                            sp[i] = sp[i].add(new Vector3f(player.getComponent(DimensionComponent.class).dimension.x / 2, 0.0f, 0.0f)).add(player.getComponent(PositionComponent.class).position);

                            if (((IKEndEffector) a.target).label.startsWith("hand")) {
                                sp[i] = sp[i].add(player.shoulderOffset);
                            }
                        }

                        DebugUtils.drawSpline(sp);
                    }
                }
            }
        }

    }
}