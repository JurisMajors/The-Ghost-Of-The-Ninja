package group4.ECS.entities.totems;

import group4.AI.Brain;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.levelSystem.Level;
import group4.maths.Vector3f;

public class StartTotem extends Totem {
    private Brain ghostBrain = null;

    public StartTotem(Vector3f position, String name, Level level, String ghostDir) {
        super(position, name, level);
        setGhost(ghostDir);
    }

    private void setGhost(String dir) {
        if (dir != null) {
            this.ghostBrain = new Brain(dir + this.name.substring(1));
        } else {
            System.err.println("[WARNING] not loading ghost for totem " + this.getID());
        }
    }

    public Ghost getChallangeGhost(Player master) {
        master.challanging = true;
        Ghost g = new Ghost(this.getComponent(PositionComponent.class).position,
                level, this.ghostBrain, master);
        g.endTotem = this.getID();
        return g;
    }

    public Ghost getHelpGhost(Player master) {
        Ghost g = new Ghost(this.getComponent(PositionComponent.class).position,
                level, this.ghostBrain, master);
        g.endTotem = this.getID();
        g.helping = true;
        return g;
    }

    public Ghost getCarryGhost(Player master) {
        Ghost g = new Ghost(this.getComponent(PositionComponent.class).position,
                this.level, this.ghostBrain, master);
        g.endTotem = this.getID();
        g.carrying = true;
       return g;
    }


    @Override
    public boolean isEnd() {
        return false;
    }

    public static String getName() {
        return "totemStart";
    }

}
