package group4.graphics;

/**
 * Creates and stores mappings from 32x32 textures tiles (embedded in larger tilemaps) to
 * st texture coordinates.
 *
 * The general idea is that we can later extend this to import designed level data (e.g. JSON) from Unity.
 */
public class TileMapping {
    public static TileMapping MAIN; // For now only 1. This class would make the game skinnable as well :-)

    // Example tiles we want to store.
    // TODO: Determine what tiles we actually need.
    public float[] FLOOR1, FLOOR2, FLOOR3; // E.g. 3 types of floors.. etc
    public float[] WALL_LEFT1, WALL_LEFT2, WALL_LEFT3;
    public float[] WALL_CORNER_LEFT1, WALL_CORNER_LEFT2, WALL_CORNER_LEFT3;

    /**
     * Loads all TileMapping objects we need as statics into this holder class.
     *
     * For every TileMapping we can set all the tiles we have to specific textureCoordinates.
     */
    public static void loadAllTileMappings() {
        // MAIN is currently 8 x 3
        MAIN = new TileMapping();


        // The idea is that we could get this data from unity
        MAIN.FLOOR1 = generateTexCoords(8, 3, 1, 1);
        MAIN.WALL_LEFT1 = generateTexCoords(8, 3, 0, 0);
        MAIN.WALL_CORNER_LEFT1 = generateTexCoords(8, 3, 0, 1);
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
     * @param x         Integer, horizontal index of the desired tile
     * @param y         Integer, vertical index of the desired tile
     * @return Float[], denoting the st (texture) coordinates of the tile in the tilemap
     */
    private static float[] generateTexCoords(int numTilesX, int numTilesY, int x, int y) {
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
