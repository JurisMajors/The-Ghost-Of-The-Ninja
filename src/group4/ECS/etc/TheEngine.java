package group4.ECS.etc;

import com.badlogic.ashley.core.Engine;

import java.util.HashMap;

/**
 * The one and only engine for ECS (Singleton Design Pattern)
 */
public class TheEngine {

    /**
     * ThreadID -> Engine Instance
     **/
    private static HashMap<Long, Engine> threadEngines = new HashMap<>();

    public synchronized static Engine getInstance() {
        long ID = Thread.currentThread().getId(); // get the id of the thread that this was called from
        // if it hasnt been called on that thread before
        if (!threadEngines.containsKey(ID) || threadEngines.get(ID) == null) {
            // create a new engine for that thread
            threadEngines.put(ID, new Engine());
        }
        // give the engine of the thread to the process
        return threadEngines.get(ID);
    }
}
