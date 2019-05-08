package group4.ECS.etc;

import com.badlogic.ashley.core.Engine;

/**
 * The one and only engine for ECS (Singleton Design Pattern)
 */
public class TheEngine {

    private static Engine engine = new Engine();

    public static Engine getInstance() {
        return engine;
    }

}
