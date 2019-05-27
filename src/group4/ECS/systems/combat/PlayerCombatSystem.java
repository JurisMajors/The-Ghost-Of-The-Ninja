package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.PlayerComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.components.stats.RangeWeaponComponent;
import group4.ECS.entities.MeleeArea;
import group4.ECS.entities.items.Item;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.input.KeyBoard;
import group4.maths.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class PlayerCombatSystem extends IteratingSystem {

    public PlayerCombatSystem(int priority) {
        super(Families.playerFamily, priority);
    }

    /**
     * This manages the combat of the player, i.e. input -> attack
     * as well as all cooldowns of players items in inventory
     *
     * @param entity player
     * @param deltaTime time in between past and current tick
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        PlayerComponent plc = Mappers.playerMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);

        // process cooldown for all items
        cooldown(deltaTime, plc);

        // if active item is a weapon
        // TODO: also other items
        MeleeWeaponComponent wc = Mappers.meleeWeaponMapper.get(plc.inventory[0]);

        // when player hits enter, attack
        if (wc != null && KeyBoard.isKeyDown(GLFW_KEY_ENTER)) {

            // if melee
            if (wc.cooldown <= 0.0f) {
                // set cooldown in accordance to rate of attack
                wc.cooldown = 1 / wc.rateOfAttack;

                // TODO: account for y
                Vector3f hitboxOffset = wc.hitboxOffset.scale(mc.orientation);
                Vector3f position = pc.position.add(hitboxOffset);
                new MeleeArea(position, wc.hitBox,
                        wc.damage, entity);
            }
        }
    }

    /**
     * This method manages all cooldowns for all items within the players inventory
     *
     * @param deltaTime time in between past and current tick
     * @param plc referencing the player
     */
    private void cooldown(float deltaTime, PlayerComponent plc) {
        for (Item item : plc.inventory) {
            // if itemslot is not used
            if (item == null) {
                continue;
            }

            // get mappers
            MeleeWeaponComponent wc = Mappers.meleeWeaponMapper.get(item);
            RangeWeaponComponent rc = Mappers.rangeWeaponMapper.get(item);

            // if item is a weapon
            if (wc != null || rc != null) {
                // update cooldown
                wc.cooldown = wc.cooldown - deltaTime;

                // if there is no cooldown on weapon
                if (wc.cooldown <= 0.0f) {
                    // set cooldown to 0, i.e. weapon can be used
                    wc.cooldown = 0.0f;
                }
            }
        }
    }

}
