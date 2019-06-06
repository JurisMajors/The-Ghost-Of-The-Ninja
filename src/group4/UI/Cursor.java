package group4.UI;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;

public class Cursor {
    public Cursor(String imageFilePath) {
        InputStream stream = new FileInputStream("C:\\path\\to\\image.png");
        BufferedImage image = ImageIO.read(stream);

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        // convert image to RGBA format
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int pixel = pixels[y * width + x];

                buffer.put((byte) ((pixel >> 16) & 0xFF));  // red
                buffer.put((byte) ((pixel >> 8) & 0xFF));   // green
                buffer.put((byte) (pixel & 0xFF));          // blue
                buffer.put((byte) ((pixel >> 24) & 0xFF));  // alpha
            }
        }
        buffer.flip(); // this will flip the cursor image vertically

        // create a GLFWImage
        GLFWImage cursorImg= GLFWImage.create();
        cursorImg.width(width);     // set up image width
        cursorImg.height(height);   // set up image height
        cursorImg.pixels(buffer);   // pass image data

        // the hotspot indicates the displacement of the sprite to the
        // position where mouse clicks are registered (see image below)
        int hotspotX = 3;
        int hotspotY = 6;

        // create custom cursor and store its ID
        long cursorID = GLFW.glfwCreateCursor(cursorImg, hotspotX , hotspotY);
    }


    // set current cursor
    glfwSetCursor(window, cursorID);
}
