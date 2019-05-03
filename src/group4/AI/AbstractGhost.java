public abstract class AbstractGhost {
    AbstractMutation mutator;
    AbstractCrossover reproducer;

    AbstractGhost(AbstractMutation m, AbstractCrossover c) {
        this.mutator = m;
        this.reproducer = c;
    }

    AbstractGhost(AbstractGhost g) {
        this(g.mutator, g.reproducer);
    }

    /**
     * Determines the next move of the ghost given a game state
     * @param gs the current game state
     * @returns move to make
     */
    public abstract String getMove(); //TODO: GameState);

    /**
     * {@link AbstractMutation}
     */
    public void mutate() {
        mutator.mutate(this);
    }

    /**
     * {@link AbstractCrossover}
     */
    public AbstractGhost crossover(AbstractGhost g) {
        return reproducer.crossover(this, g);
    }
}
