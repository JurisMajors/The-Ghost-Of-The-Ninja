package group4.levelSystem.levels;

import group4.ECS.entities.Player;
import group4.ECS.entities.world.Exit;
import group4.levelSystem.ExitAction;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;

public class AILevel extends Level {
    private String modulePath;

    AILevel (String path) {
        super();
        this.modulePath = path;
    }

    @Override
    protected Module createRoot() {
        if (modulePath == null) throw new IllegalStateException("Module Path is not set for AI level");
        return new Module(this, this.modulePath, null);
    }

    @Override
    protected void createAdditionalModules() {

    }

    @Override
    protected Player createPlayer() {
        return new Player(new Vector3f(), this);
    }

    @Override
    protected void configExits() {
        ExitAction global = new ExitAction() {
            @Override
            public void exit() {
                System.out.println("Ghost has reached the exit");
            }
        };

        for (Module m : this.modules) {
            for (Exit e : m.getExits()) {
                this.setExitAction(e, global);
            }
        }
    }
}
