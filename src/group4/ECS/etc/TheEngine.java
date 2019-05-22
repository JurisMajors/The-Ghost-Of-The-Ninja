package group4.ECS.etc;

import com.badlogic.ashley.core.Engine;

import java.util.HashMap;

/**
 * The one and only engine for ECS (Singleton Design Pattern)
 */
public class TheEngine {

    private static HashMap<Long, Engine> threadEngines = new HashMap<>();

    public static Engine getInstance() {
        long ID = Thread.currentThread().getId();
        if (!threadEngines.containsKey(ID)) {
            threadEngines.put(ID, new Engine());
        }
        return threadEngines.get(ID);
    }
}
