package group4.ECS.systems;

import group4.ECS.etc.Families;

public class GhostDyingSystem extends PlayerDyingSystem {

    public GhostDyingSystem (boolean reset) {
        super(Families.ghostFamily, reset);
    }

    public GhostDyingSystem () {
        super(Families.ghostFamily, false);
    }

}
