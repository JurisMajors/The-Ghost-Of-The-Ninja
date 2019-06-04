package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;

public class ScoreComponent implements Component {

    private int score;

    /**
     * Creates a ScoreComponent that stores a score.
     *
     * @param initScore initial score
     */
    public ScoreComponent(int initScore) {
        this.score = initScore;
    }

    /**
     * Creates a ScoreComponent with 0 score
     */
    public ScoreComponent() {
        this(0);
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public void addScore(int addition) {
        this.score += addition;
    }

    public void subScore(int subtraction) {
        this.score -= subtraction;
    }

}
