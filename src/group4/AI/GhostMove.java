package group4.AI;

/**
 * Defines the moves as integers for the ghost
 */
public enum GhostMove {

    LEFT (0),
    RIGHT (1),
    JUMP (2);

    private int move;

    GhostMove(int move) {
        this.move = move;
    }

    public boolean equals (Integer i) {
        return i.equals(this.move);
    }

}
