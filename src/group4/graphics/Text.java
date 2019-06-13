package group4.graphics;

import group4.ECS.components.GraphicsComponent;
import group4.maths.Vector3f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.max;

public class Text {
    private Map<Character, Glyph> atlas;
    private Texture texture;
    private int fontHeight;
    private int atlasWidth, atlasHeight;

    public Text(String path) {
        this.atlas = new HashMap<>();
        try {
//            Font font = new Font ("TimesRoman", Font.BOLD, 32);
            Font font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(path));
            font = font.deriveFont(35F);
            this.texture = createAtlas(font);
        } catch (Exception e) {
            System.err.println("[WARNING] Could not load font!");
        }
    }

    private Texture createAtlas(Font font) {
        int width = 0;
        int height = 0;

        // We skip 0 to 31 as they are just ascii control codes
        for (int i = 32; i < 256; i++) {
            if (i == 127) continue; // Another control code

            char c = (char) i;
            BufferedImage glyphImage = createGlyphImage(font, c);

            width += glyphImage.getWidth(); // Update the width of the image to store this additional character
            height = max(height, glyphImage.getHeight()); // Update the height the image should be
        }

        fontHeight = height;

        BufferedImage atlasImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = atlasImage.createGraphics();

        int x = 0; // For tracking the start position of the next character in the atlas

        // We skip 0 to 31 as they are just ascii control codes
        for (int i = 32; i < 256; i++) {
            if (i == 127) continue; // Another control code

            char c = (char) i;
            BufferedImage glyphImage = createGlyphImage(font, c);
            int glyphWidth = glyphImage.getWidth();
            int glyphHeight = glyphImage.getHeight();

            Glyph glyph = new Glyph(glyphWidth, glyphHeight, x, atlasImage.getHeight() - glyphHeight, 0.0f);
            g.drawImage(glyphImage, x, 0, null);
            x += glyph.width;
            atlas.put(c, glyph);
        }

        this.atlasWidth = atlasImage.getWidth();
        this.atlasHeight = atlasImage.getHeight();

        return new Texture(atlasImage);
    }

    /**
     * @param font
     * @param c
     * @return
     */
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

    /**
     * Draw text at the specified position and color.
     *
     * @param text     Text to draw
     * @param x        X coordinate of the text position
     * @param y        Y coordinate of the text position
     */
    public void drawText(String text, float x, float y) {
        int textHeight = getHeight(text);

        float drawX = x;
        float drawY = y;
        if (textHeight > fontHeight) {
            drawY += textHeight - fontHeight;
        }

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                /* Line feed, set x and y to draw at the next line */
                drawY -= fontHeight;
                drawX = x;
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }

            // Create the texture and draw!
            Glyph g = this.atlas.get(ch);

            float[] glyphTexCoords = new float[]{
                g.x / (float) atlasWidth, (g.y + g.height) / (float) atlasHeight,
                g.x / (float) atlasWidth, g.y / (float) atlasHeight,
                (g.x + g.width) / (float) atlasWidth, g.y / (float) atlasHeight,
                (g.x + g.width) / (float) atlasWidth, (g.y + g.height) / (float) atlasHeight
            };

            GraphicsComponent tile = new GraphicsComponent(
                    Shader.SIMPLE,
                    this.texture,
                    new Vector3f((float) g.width / 100, (float)g.height / 100, 0.0f),
                    glyphTexCoords,
                    false
                );
            tile.flush(new Vector3f(drawX, drawY, 0.0f));
            drawX += g.width / 100.0f;
        }
    }
}
