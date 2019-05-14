package group4.graphics;

public class TileMapping {
    public static TileMapping MAIN;

    public float[] FLOOR1, FLOOR2, FLOOR3;
    public float[] WALL_LEFT1, WALL_LEFT2, WALL_LEFT3;
    public float[] WALL_CORNER_LEFT1, WALL_CORNER_LEFT2, WALL_CORNER_LEFT3;

    public static void loadAllTileMappings() {
        // MAIN is currently 8 x 3
        MAIN = new TileMapping();


        // The idea is that we could get this data from unity
        MAIN.FLOOR1 = generateTexCoords(8, 3, 1, 1);
    }

    private static float[] generateTexCoords(int numTilesX, int numTilesY, int x, int y) {
        float hStep = 1.0f / numTilesX;
        float vStep = 1.0f / numTilesY;
        return new float[] {
                x * hStep, y * vStep,
                (x + 1) * hStep, y * vStep,
                (x + 1) * hStep, (y + 1) * vStep,
                x * hStep, (y + 1) * vStep
        };
    }
}
