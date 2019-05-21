package group4.ECS.systems;

import group4.ECS.etc.Families;

public class JumpingWalkingMobMovementSystem extends MobMovementSystem {

    public JumpingWalkingMobMovementSystem() {
        super(Families.jumpingWalkingMobFamily);
    }

}
