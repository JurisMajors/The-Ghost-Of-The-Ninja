package group4.ECS.components.identities;

import com.badlogic.ashley.core.Component;
import group4.ECS.etc.EntityConst;

public class ConsumableComponent implements Component {

    // non-defined item, itemID's stored in etc.EntityConst
    public EntityConst.ItemID itemID = EntityConst.ItemID.UNDEFINED_ITEM;

    // non-defined effect, itemID's stored in etc.EntityConst
    public EntityConst.EffectID effectID = EntityConst.EffectID.UNDEFINED_EFFECT;

    public ConsumableComponent(EntityConst.ItemID itemID, EntityConst.EffectID effectID) {
        this.itemID = itemID;
        this.effectID = effectID;
    }

}
