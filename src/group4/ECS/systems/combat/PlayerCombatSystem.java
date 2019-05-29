package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.PlayerComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.ECS.components.stats.RangeWeaponComponent;
import group4.ECS.entities.MeleeArea;
import group4.ECS.entities.items.Item;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.ECS.etc.TheEngine;
import group4.game.Main;
import group4.game.Window;
import group4.input.KeyBoard;
import group4.input.MouseClicks;
import group4.input.MouseMovement;
import group4.maths.Vector3f;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

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
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);

        // process cooldown for all items
        cooldown(deltaTime, plc);

        // check if weapon was switched
        switchWeapon(deltaTime, plc);

        // if slot is empty, i.e. null, do nothing
        if (plc.activeItem == null) {
            return;
        }

        // if active item is a weapon and when player hits enter, attack
        MeleeWeaponComponent wc = Mappers.meleeWeaponMapper.get(plc.activeItem);
        if (wc != null && MouseClicks.leftMouseDown()) {
            System.out.println(plc.activeItem);

            // if melee
            if (wc.cooldown <= 0.0f) {
                // set cooldown in accordance to rate of attack
                wc.cooldown = 1 / wc.rateOfAttack;

                System.out.println(wc.hitBox);
                // camera x in world position
                float camX = TheEngine.getInstance().getEntitiesFor(Families.cameraFamily).get(0)
                        .getComponent(PositionComponent.class).position.x;

                // mouse x in world pos
                float mouseWorldX = camX + ((float) MouseMovement.mouseX *
                        (Main.SCREEN_WIDTH / Window.getWidth()) - Main.SCREEN_WIDTH / 2);

                // TODO: account for y
                // if clicking right of player, hit right, else hit left
                Vector3f hitboxOffset;
                if (mouseWorldX >= pc.position.x + dc.dimension.x / 2) {
                    hitboxOffset = wc.hitboxOffset.scale(1);
                } else {
                    hitboxOffset = wc.hitboxOffset.scale(-1);
                }

                // exclude ghost and player for the damage
                Set<Entity> excluded = new HashSet<>();
                excluded.add(entity);
                excluded.add(TheEngine.getInstance().getEntitiesFor(Families.ghostFamily).get(0));

                Vector3f position = pc.position.add(hitboxOffset);
                new MeleeArea(position, wc.hitBox,
                        wc.damage, excluded);
            }
        }
    }

    /**
     * This method manages all cooldowns for all items within the players inventory
     *
     * @param deltaTime time in between past and current tick
     * @param plc referencing the player
     * TODO: add cooldown on weaponswitch
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

    /**
     * This method manages which weapon is currently active
     *
     * @param deltatime
     * @param plc
     */
    private void switchWeapon(float deltatime, PlayerComponent plc) {
        if (KeyBoard.isKeyDown(GLFW_KEY_1)) {
            plc.activeItem = plc.inventory[0];
        } else if (KeyBoard.isKeyDown(GLFW_KEY_2)) {
            plc.activeItem = plc.inventory[1];
        } else if (KeyBoard.isKeyDown(GLFW_KEY_3)) {
            plc.activeItem = plc.inventory[2];
        } else if (KeyBoard.isKeyDown(GLFW_KEY_4)) {
            plc.activeItem = plc.inventory[3];
        } else if (KeyBoard.isKeyDown(GLFW_KEY_5)) {
            plc.activeItem = plc.inventory[4];
        } else if (KeyBoard.isKeyDown(GLFW_KEY_6)) {
            plc.activeItem = plc.inventory[5];
        } else if (KeyBoard.isKeyDown(GLFW_KEY_7)) {
            plc.activeItem = plc.inventory[6];
        } else if (KeyBoard.isKeyDown(GLFW_KEY_8)) {
            plc.activeItem = plc.inventory[7];
        }
    }

}
