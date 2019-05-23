package group4.levelSystem;

public abstract class ExitAction {

    protected Level callBackLevel;

    /**
     * Constructor to construct an Exit Action with a level to call back to
     * @param l the level to call back to
     */
    public ExitAction(Level l) {
        this.callBackLevel = l;
    }


    /**
     * Constructor to construct an Exit Action without a callback level
     */
    public ExitAction() {}


    /**
     * Override this method to set the exit action command
     */
    public abstract void exit();

}
