package group4.ECS.etc;

/**
 * constant ID's for item, trap, enemy, ... related game mechanics
 * <p>
 * not final, we might in the end not use enums....
 */
public class EntityConst {

    // TODO: add items
    public enum ItemID {
        UNDEFINED_ITEM,
        HEALTH_POTION,
        PISTOL
    }

    // TODO: add effects
    public enum EffectID {
        UNDEFINED_EFFECT,
        SMALL_HEAL,
        MEDIUM_HEAL,
        LARGE_HEAL,
        SMALL_POISON
    }

    // TODO: add bullet types
    public enum BulletType {
        UNDEFINED_TYPE,
        PISTOL,
        MACHINEGUN,
    }

    // defines gameplay related states
    public enum EntityState {
        IMMUNE,         // immune against dmg
        KNOCKED         // knocked back, so not be able to move
    }

    public enum MobState {
        MELEE,
        RANGED,
        DEFAULT
    }

}
