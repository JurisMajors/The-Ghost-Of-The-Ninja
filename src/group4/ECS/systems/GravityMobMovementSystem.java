package group4.ECS.systems;

import group4.ECS.etc.Families;

public class GravityMobMovementSystem extends MobMovementSystem {

    public GravityMobMovementSystem() {
        super(Families.movingGravityMobFamily);
    }

}
