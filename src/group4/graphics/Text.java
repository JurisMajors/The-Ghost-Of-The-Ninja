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
}
