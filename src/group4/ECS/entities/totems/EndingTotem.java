package group4.ECS.entities.totems;

import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class EndingTotem extends Totem {

    public EndingTotem(Vector3f position, String name, Level level) {
        super(position, name, level);
    }

    @Override
    public boolean isEnd() {
        return true;
    }

    public static String getName() {
        return "totemEnd";
    }
}
