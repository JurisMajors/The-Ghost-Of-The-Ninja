public abstract class AbstractCrossover {
    /**
     * Produces a new {@link AbstractGhost} by the reproducer crossover rule
     * The two ghosts are not modified.
     * @param g1 first ghost to crossover with
     * @param g2 other ghost to crossover with
     * @return the resulting ghost of the crossover rule
     **/
    public abstract AbstractGhost crossover(AbstractGhost g1, AbstractGhost g2);
}
