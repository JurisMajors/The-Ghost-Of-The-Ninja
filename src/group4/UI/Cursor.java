package group4.UI;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Cursor {
    private long cursor;

    public Cursor(String imageFilePath) {
        try {
            InputStream stream = new FileInputStream(imageFilePath);

            BufferedImage image = ImageIO.read(stream);
            int width = image.getWidth();
            int height = image.getHeight();

            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            // convert image to RGBA format
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));  // R
                    buffer.put((byte) ((pixel >> 8) & 0xFF));   // G
                    buffer.put((byte) (pixel & 0xFF));          // B
                    buffer.put((byte) ((pixel >> 24) & 0xFF));  // A
                }
            }
            buffer.flip(); // this will flip the cursor image vertically

            // create a GLFWImage
            GLFWImage cursorImg = GLFWImage.create();
            cursorImg.width(width);     // set up image width
            cursorImg.height(height);   // set up image height
            cursorImg.pixels(buffer);   // pass image data

            // create custom cursor and store its ID
            cursor = GLFW.glfwCreateCursor(cursorImg, 8, 8);
        } catch (Exception e) {
            System.err.println("[ERROR] " + e);
        }
    }

    public long getCursorId() {
        return this.cursor;
    }
}
