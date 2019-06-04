package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.PlayerComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.ItemComponent;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.damage.MeleeArea;
import group4.ECS.entities.Player;
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

            // if melee
            if (wc.currCooldown <= 0.0f) {
                // set cooldown in accordance to rate of attack
                wc.currCooldown = wc.cooldown;

                // TODO: account for non-centric camera, e.g. pass on cam offset from display centre
                // camera x in world position
                float camX = TheEngine.getInstance().getEntitiesFor(Families.cameraFamily).get(0)
                        .getComponent(PositionComponent.class).position.x;

                // mouse x in world pos
                float mouseWorldX = camX + ((float) MouseMovement.mouseX *
                        (Main.SCREEN_WIDTH / Window.getWidth()) - Main.SCREEN_WIDTH / 2);

                // if clicking right of player, hit right, else hit left
                Vector3f trueOffset = new Vector3f(wc.hitboxOffset);
                if (mouseWorldX < pc.position.x + dc.dimension.x / 2) {
                    trueOffset.x = -1 * wc.hitboxOffset.x;
                }
                // TODO:
                // cast ray to the center of the hitbox
                // see if it doesnt hit walls/static objects
                // change offset accordingly

                // exclude ghost and player for the damage
                Set<Class<? extends Entity>> excluded = new HashSet<>();
                excluded.add(Player.class);
                excluded.add(Ghost.class);

                Vector3f position = pc.position.add(trueOffset);
                new MeleeArea(position, wc.hitBox,
                        wc.damage, excluded);
            }
        }
    }

    /**
     * This method manages all cooldowns for all items within the players inventory
     *
     * @throws IllegalStateException if item in inventory does not have an {@link ItemComponent}
     * @param deltaTime time in between past and current tick
     * @param plc referencing the player
     */
    private void cooldown(float deltaTime, PlayerComponent plc) throws IllegalStateException {
        for (Entity item : plc.inventory) {
            // if itemslot is not used
            if (item == null) {
                continue;
            }

            // hacky way to get the super component to itemcomponent
            ItemComponent ic = null;
            for (Component c : item.getComponents()) {
                if (c instanceof ItemComponent) {
                    ic = (ItemComponent) c;
                }
            }

            System.out.println(ic);

            // if item slot used, but not item, throw exception
            if (ic == null) {
                throw new IllegalStateException("Invalid object in inventory: " + item.getClass());
            }

            // if there is cooldown on the item
            if (ic.currCooldown >= 0.0f) {
                // update cooldown
                ic.currCooldown -= deltaTime;
            }
        }
    }

    /**
     * This method manages which weapon is currently active
     * We assume items in the inventory to have an {@link ItemComponent}
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
