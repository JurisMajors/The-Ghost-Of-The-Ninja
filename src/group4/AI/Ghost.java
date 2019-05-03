package group4.AI;

/**
 * The AI Player, passed to the game package.
 */
public class Ghost { //TODO: extends Player
    Brain brain;

    Ghost(Brain b) {
        this.brain = b;
    }
}
