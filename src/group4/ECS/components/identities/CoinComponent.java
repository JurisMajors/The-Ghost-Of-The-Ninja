package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;

public class CoinComponent implements Component {

    /**
     * Score that this coin gives
     */
    public int value;

    public CoinComponent(int value) {
        this.value = value;
    }
}
