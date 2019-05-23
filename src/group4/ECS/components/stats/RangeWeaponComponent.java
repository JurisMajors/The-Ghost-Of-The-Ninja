package group4.ECS.components.stats;

import com.badlogic.ashley.core.Component;
import group4.maths.Vector3f;

public class RangeWeaponComponent extends WeaponComponent {

    public Vector3f bulletPos; //position where the bullets appear with respect to the entity position
    public int rate; //number of frames between two consecutive shots
    public int wait; //used to count frames, shoot when wait=rate

    public RangeWeaponComponent(int damage, Vector3f gripPos, Vector3f bulletPos) {
        super(damage, gripPos);
        this.bulletPos = bulletPos;
        this.rate = 1;
        this.wait = 1;
    }

    public RangeWeaponComponent(int damage, int rate, Vector3f gripPos, Vector3f bulletPos) {
        super(damage, gripPos);
        this.bulletPos = bulletPos;
        this.rate = rate;
        this.wait = rate;
    }

}
