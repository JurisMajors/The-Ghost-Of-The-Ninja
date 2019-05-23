package group4.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.max;
// https://github.com/SilverTiger/lwjgl3-tutorial/blob/master/src/silvertiger/tutorial/lwjgl/text/Font.java
public class Text {
    private Texture createAtlas(Font font) {
        int width = 0;
        int height = 0;
        
        for (int i = 32; i < 256; i++) {
            if (i == 127) continue;
            
            char c = (char) i;
            BufferedImage glyphImage = createGlyphImage(font, c);

            width += glyphImage.getWidth();
            height = max(height, glyphImage.getHeight());
        }

//        fontHeight = height;
    }

    private BufferedImage createGlyphImage(Font font, char c) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // Optional??
        Graphics2D g = image.createGraphics(); // Container which enables us to draw into
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();

        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();

        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics(); // Optional to do again?
        g.setFont(font);
        g.setPaint(Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        return image;
    }

    /**
     * Gets the width of the specified text.
     *
     * @param text String, the text to get the height of.
     * @return Integer, width of the line of text in pixels.
     */
    public int getWidth(String text) {
        int width = 0;
        int lineWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                /* Line end, set width to maximum from line width and stored
                 * width */
                width = max(width, lineWidth);
                lineWidth = 0;
                continue;
            }
            if (c == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph glyph = this.atlas.get(c);
            lineWidth += glyph.width;
        }
        width = max(width, lineWidth);
        return width;
    }

    /**
     * Gets the height of the specified text.
     *
     * @param text String, the text to get the height of.
     * @return Integer, height of the line of text in pixels.
     */
    public int getHeight(String text) {
        int height = 0;
        int lineHeight = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                /* Line end, add line height to stored height */
                height += lineHeight;
                lineHeight = 0;
                continue;
            }
            if (c == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph g = this.atlas.get(c);
            lineHeight = Math.max(lineHeight, g.height);
        }
        height += lineHeight;
        return height;
    }
}
