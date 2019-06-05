package group4.AI.decoders;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.stats.MovementComponent;
import group4.maths.Vector3f;
import org.json.JSONObject;

/**
 * Starts the ray casting on an angle, relative to the velocity of the ghost
 */
public class ConeVisionStateDecoder extends RayStateDecoder{

    public ConeVisionStateDecoder(int n, float angleRange, int length) {
        super(n, angleRange, length);
    }

    @Override
    Vector3f getCastingStart(Entity ghost) {
        Vector3f ghostVel = ghost.getComponent(MovementComponent.class).velocity.normalized();
        Vector3f start = ghostVel.rotateXY(-1 * this.angleRange / 2.0f);
        start.normalizei();
        return start;
    }

    @Override
    public JSONObject getSettings() {
        JSONObject settings = new JSONObject();
        settings.put("rays", this.nrRays);
        settings.put("range", this.angleRange);
        settings.put("raylength", this.rayLength);
        return settings;
    }

    /**
     * Initialize based on given settings
     * @param settings jsonObject of settings to load in this decoder
     */
    public static ConeVisionStateDecoder loadOnSettings(JSONObject settings) {
        int nrRays = settings.getInt("rays");
        int range = settings.getInt("range");
        int lngth = settings.has("raylength") ? settings.getInt("raylength") : 10000;
        return new ConeVisionStateDecoder(nrRays, range, lngth);
    }
}
