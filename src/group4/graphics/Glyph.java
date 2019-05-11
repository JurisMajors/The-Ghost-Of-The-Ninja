package group4.graphics;

public class Glyph {
    public final int width, height;
    public final int x, y;

    /**
     * Glyph constructor for storing the exact location of a glyph on an atlas containing
     * all the characters of a specific font.
     *
     * @param width Integer, width of the glyph
     * @param height Integer, height of the glyph
     * @param x Integer, x position on the atlas
     * @param y Integer, y position on the atlas
     */
    public Glyph(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
}
