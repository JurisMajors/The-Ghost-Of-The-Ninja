package group4.AI.decoders;

import com.badlogic.ashley.core.Entity;
import group4.maths.Vector3f;
import org.json.JSONObject;

/**
 * Casts rays around the ghost
 */
public class CircleVisionStateDecoder extends RayStateDecoder implements StateDecoderInterface {

    private int gap;
    /**
     * @param n number of rays to be casted
     * @param angleGap the gap in degrees between the first and the last ray
     *
     */
    public CircleVisionStateDecoder(int n, int angleGap, int length) {
        super(n, 360.0f - angleGap, length);
        this.gap = angleGap / 2;
    }

    @Override
    Vector3f getCastingStart(Entity ghost) {
        // vector representing the x-axis unit vector, which is at 0 degrees
        Vector3f start = new Vector3f(1, 0, 0);
        // rotate downwards considering the gap
        start.rotateXYi(-90 + this.gap);
        // normalize just for safety
        start.normalizei();
        return start;
    }

    @Override
    public JSONObject getSettings() {
        JSONObject settings = new JSONObject();
        settings.put("rays", this.nrRays);
        settings.put("gap", this.gap);
        return settings;
    }

    /**
     * Initialize based on given settings
     * @param settings jsonObject of settings to load in this decoder
     */
    public static CircleVisionStateDecoder loadOnSettings(JSONObject settings) {
        int nrRays = settings.getInt("rays");
        int gap = settings.getInt("gap");
        int lngth = settings.has("raylength") ? settings.getInt("raylength") : 10000;
        return new CircleVisionStateDecoder(nrRays, gap, lngth);
    }
}
