package group4.graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates and stores mappings from 32x32 textures tiles (embedded in larger tilemaps) to
 * st texture coordinates.
 *
 * The general idea is that we can later extend this to import designed level data (e.g. JSON) from Unity.
 */
public class TileMapping {
    public static Map<Integer, float[]> MAIN; // For now only 1. This class would make the game skinnable as well :-)
    public static int MAIN_SIZE = 8 * 7; // Important! Update this when the MAIN tilemap changes its dimensions.
    /**
     * Loads all TileMapping objects we need as statics into this holder class.
     *
     * For every TileMapping we can set all the tiles we have to specific textureCoordinates.
     */
    public static void loadAllTileMappings() {
        // MAIN is currently 8 x 7
        MAIN = new HashMap<>();
        for (int i = 0; i < 64; i++) {
            MAIN.put(i, generateTexCoords(8, 7, i));
        }
    }

    /**
     * Based on the parameters returns the correct st (texture) coordinates of a given tile in a respective tilemap.
     * <p>
     * Note: topleft in the image is (0,0).
     * + --- + --- +
     * | 0,0 | 1,0 |
     * + --- + --- +
     * | 1,0 | 1,1 |
     * + --- + --- +
     *
     * @param numTilesX Integer, number of tiles in the tilemap image horizontally
     * @param numTilesY Integer, number of tiles in the tilemap image vertically
     * @param i         Integer, index of the desired tile
     * @return Float[], denoting the st (texture) coordinates of the tile in the tilemap
     */
    private static float[] generateTexCoords(int numTilesX, int numTilesY, int i) {
        int x = i % numTilesX;
        int y = i / numTilesX;
        float hStep = 1.0f / numTilesX;
        float vStep = 1.0f / numTilesY;
        return new float[]{
                x * hStep, (y + 1) * vStep,
                x * hStep, y * vStep,
                (x + 1) * hStep, y * vStep,
                (x + 1) * hStep, (y + 1) * vStep
        };
    }
}
