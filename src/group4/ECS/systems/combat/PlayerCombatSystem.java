package group4.ECS.systems.combat;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import group4.ECS.components.identities.PlayerComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MeleeWeaponComponent;
import group4.ECS.components.stats.WeaponComponent;
import group4.ECS.entities.DamageArea;
import group4.ECS.entities.items.Item;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.input.KeyBoard;
import group4.maths.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class PlayerCombatSystem extends IteratingSystem {

    public PlayerCombatSystem() { super(Families.playerFamily); }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pc = Mappers.positionMapper.get(entity);
        PlayerComponent plc = Mappers.playerMapper.get(entity);

//        // if damage is inflicted by rangeweapon
//        PositionComponent playerPos = Mappers.positionMapper.get(TheEngine.getInstance()
//                .getEntitiesFor(Families.playerFamily).get(0));
//        if (rwc.wait < rwc.rate) rwc.wait++; //count frames until next shot
//        else { //shoot
//            rwc.wait = 0;
//            //create new bullet
//            new Bullet(pc.position.add(rwc.bulletPos),
//                    playerPos.position.sub(pc.position.add(rwc.bulletPos)).normalized());
//        }


        for (Item item : plc.inventory) {
            WeaponComponent wc = Mappers.weaponMapper.get(item);

            // if item is a weapon
            if (wc != null) {
                // update cooldown
                wc.cooldown = wc.cooldown - deltaTime;

                // if there is no cooldown on weapon
                if (wc.cooldown <= 0.0f) {
                    // set cooldown to 0, i.e. weapon can be used
                    wc.cooldown = 0.0f;

                    // when player hits enter, attack
                    if (KeyBoard.isKeyDown(GLFW_KEY_ENTER)) {
                        // set cooldown in accordance to rate of attack
                        wc.cooldown = 1 / wc.rateOfAttack;

                        System.out.println(wc.getClass());
                        // if melee
                        if (wc.getClass().equals(MeleeWeaponComponent.class)) {
                            Vector3f position = pc.position.add(((MeleeWeaponComponent)wc).hitboxOffset);
                            //TODO: orientation
                            new DamageArea(position, ((MeleeWeaponComponent)wc).hitBox,
                                    wc.damage);
                        }
                    }
                }
            }
        }

    }

}
