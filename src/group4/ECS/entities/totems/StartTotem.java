package group4.ECS.entities.totems;

import group4.ECS.entities.Ghost;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class StartTotem extends Totem {
    private Ghost ghost;

    StartTotem(Vector3f position, String name, Level level, String ghostDir) {
        super(position, name, level);
        setGhost(ghostDir);
    }

    private void setGhost(String dir) {
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    public static String getName() {
        return "totemStart";
    }

}
