package group4.graphics;

import group4.utils.BufferUtils;

import javax.imageio.ImageIO;
import javax.xml.soap.Text;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    // Storing all texture resources when preloaded.
    public static Texture DEBUG; // TODO: More to be added.
    public static Texture BRICK;
    public static Texture AK47;
    public static Texture MG_BULLET;
    public static Texture EXIT;  // TODO: More to be added.
    public static Texture BLACK;
    public static Texture WHITE;
    public static Texture GREEN;
    public static Texture RED;
    public static Texture MAIN_TILES;
    public static Texture BACKGROUND;
    public static Texture HITBOX;
    public static Texture NOTHINGNESS;
    public static Texture SPLINE;
    public static Texture TOTEM_START;
    public static Texture TOTEM_END;
    public static Texture TOTEM_HELP;

    // Storing all player related textures
    public static Texture PLAYER_TORSO;
    public static Texture PLAYER_HEAD;
    public static Texture PLAYER_DARK_LEG_UPPER;
    public static Texture PLAYER_DARK_LEG_LOWER;
    public static Texture PLAYER_LIGHT_LEG_UPPER;
    public static Texture PLAYER_LIGHT_LEG_LOWER;
    public static Texture PLAYER_DARK_ARM_UPPER;
    public static Texture PLAYER_DARK_ARM_LOWER;
    public static Texture PLAYER_LIGHT_ARM_UPPER;
    public static Texture PLAYER_LIGHT_ARM_LOWER;

    // Storing all red ghost related textures
    public static Texture RED_GHOST_TORSO;
    public static Texture RED_GHOST_HEAD;
    public static Texture RED_GHOST_DARK_LEG_UPPER;
    public static Texture RED_GHOST_DARK_LEG_LOWER;
    public static Texture RED_GHOST_LIGHT_LEG_UPPER;
    public static Texture RED_GHOST_LIGHT_LEG_LOWER;
    public static Texture RED_GHOST_DARK_ARM_UPPER;
    public static Texture RED_GHOST_DARK_ARM_LOWER;
    public static Texture RED_GHOST_LIGHT_ARM_UPPER;
    public static Texture RED_GHOST_LIGHT_ARM_LOWER;
    public static Texture RED_GHOST_LEG_BLOB;

    // Storing all blue ghost related textures
    public static Texture BLUE_GHOST_TORSO;
    public static Texture BLUE_GHOST_HEAD;
    public static Texture BLUE_GHOST_DARK_LEG_UPPER;
    public static Texture BLUE_GHOST_DARK_LEG_LOWER;
    public static Texture BLUE_GHOST_LIGHT_LEG_UPPER;
    public static Texture BLUE_GHOST_LIGHT_LEG_LOWER;
    public static Texture BLUE_GHOST_DARK_ARM_UPPER;
    public static Texture BLUE_GHOST_DARK_ARM_LOWER;
    public static Texture BLUE_GHOST_LIGHT_ARM_UPPER;
    public static Texture BLUE_GHOST_LIGHT_ARM_LOWER;
    public static Texture BLUE_GHOST_LEG_BLOB;

    
    // GAME UI / OVERLAYS
    public static Texture START_BG;
    public static Texture VIGNETTE_OVERLAY;
    public static Texture NOISE_OVERLAY;
    public static Texture PRESS_ENTER;
    public static Texture LOGO_FULLSCREEN;

    // width and height of the texture
    private int width, height;
    // internal opengl id for this texture
    private int texture;

    public Texture(String path) {
        texture = load(path);
    }

    /**
     * Function which loads in all textures as constants for easy access later on.
     * Should be executed once during game initialization.
     */
    public static void loadAllTextures() {
        DEBUG = new Texture("src/group4/res/textures/debug.jpeg");
        BRICK = new Texture("src/group4/res/textures/brick.png");
        AK47 = new Texture("src/group4/res/textures/weapons/AK47.png");
        MG_BULLET = new Texture("src/group4/res/textures/weapons/Bullet.png");
        EXIT = new Texture("src/group4/res/textures/exit.jpg");
        MAIN_TILES = new Texture("src/group4/res/textures/tilemap-main.png");
        BACKGROUND = new Texture("src/group4/res/textures/cave.jpg");
        BLACK = new Texture("src/group4/res/textures/black.png");
        WHITE = new Texture("src/group4/res/textures/white.png");
        GREEN = new Texture("src/group4/res/textures/green.png");
        RED = new Texture("src/group4/res/textures/red.png");
        HITBOX = new Texture("src/group4/res/textures/hitbox.png");
        NOTHINGNESS = new Texture("src/group4/res/textures/FF4D00-0.png");
        SPLINE = new Texture("src/group4/res/textures/spline.png");

        PLAYER_TORSO = new Texture("src/group4/res/textures/player/ninja3withlayers_0008_body.png");
        PLAYER_HEAD = new Texture("src/group4/res/textures/player/ninja3withlayers_0009_head.png");
        PLAYER_DARK_LEG_LOWER = new Texture("src/group4/res/textures/player/ninja3withlayers_0002_lowerfurtherleg.png");
        PLAYER_DARK_LEG_UPPER = new Texture("src/group4/res/textures/player/ninja3withlayers_0003_upperfurtherleg.png");
        PLAYER_LIGHT_LEG_LOWER = new Texture("src/group4/res/textures/player/ninja3withlayers_0000_lowerfurtherleg.png");
        PLAYER_LIGHT_LEG_UPPER = new Texture("src/group4/res/textures/player/ninja3withlayers_0001_uppercloserleg.png");
        PLAYER_DARK_ARM_UPPER = new Texture("src/group4/res/textures/player/ninja3withlayers_0007_upperfurtherarm.png");
        PLAYER_DARK_ARM_LOWER = new Texture("src/group4/res/textures/player/ninja3withlayers_0006_lowerfurtherarm.png");
        PLAYER_LIGHT_ARM_UPPER = new Texture("src/group4/res/textures/player/ninja3withlayers_0005_uppercloserarm.png");
        PLAYER_LIGHT_ARM_LOWER = new Texture("src/group4/res/textures/player/ninja3withlayers_0004_lowercloserarm.png");

        RED_GHOST_TORSO = new Texture("src/group4/res/textures/ghost/red/torso.png");
        RED_GHOST_HEAD = new Texture("src/group4/res/textures/ghost/red/head.png");
        RED_GHOST_DARK_LEG_LOWER = NOTHINGNESS;
        RED_GHOST_DARK_LEG_UPPER = NOTHINGNESS;
        RED_GHOST_LIGHT_LEG_LOWER = NOTHINGNESS;
        RED_GHOST_LIGHT_LEG_UPPER = NOTHINGNESS;
        RED_GHOST_DARK_ARM_UPPER = new Texture("src/group4/res/textures/ghost/red/darkarmupper.png");
        RED_GHOST_DARK_ARM_LOWER = new Texture("src/group4/res/textures/ghost/red/darkarmlower.png");
        RED_GHOST_LIGHT_ARM_UPPER = new Texture("src/group4/res/textures/ghost/red/lightarmupper.png");
        RED_GHOST_LIGHT_ARM_LOWER = new Texture("src/group4/res/textures/ghost/red/lightarmlower.png");
        RED_GHOST_LEG_BLOB = new Texture("src/group4/res/textures/ghost/red/legblob.png");

        BLUE_GHOST_TORSO = new Texture("src/group4/res/textures/ghost/blue/torso.png");
        BLUE_GHOST_HEAD = new Texture("src/group4/res/textures/ghost/blue/head.png");
        BLUE_GHOST_DARK_LEG_LOWER = NOTHINGNESS;
        BLUE_GHOST_DARK_LEG_UPPER = NOTHINGNESS;
        BLUE_GHOST_LIGHT_LEG_LOWER = NOTHINGNESS;
        BLUE_GHOST_LIGHT_LEG_UPPER = NOTHINGNESS;
        BLUE_GHOST_DARK_ARM_UPPER = new Texture("src/group4/res/textures/ghost/blue/darkarmupper.png");
        BLUE_GHOST_DARK_ARM_LOWER = new Texture("src/group4/res/textures/ghost/blue/darkarmlower.png");
        BLUE_GHOST_LIGHT_ARM_UPPER = new Texture("src/group4/res/textures/ghost/blue/lightarmupper.png");
        BLUE_GHOST_LIGHT_ARM_LOWER = new Texture("src/group4/res/textures/ghost/blue/lightarmlower.png");
        BLUE_GHOST_LEG_BLOB = new Texture("src/group4/res/textures/ghost/blue/legblob.png");

        
        TOTEM_START = new Texture("src/group4/res/textures/start-totem.png");
        TOTEM_END = new Texture("src/group4/res/textures/end-totem.png");
        START_BG = new Texture("src/group4/res/textures/start-bg.png");
        VIGNETTE_OVERLAY = new Texture("src/group4/res/textures/vignette-overlay.png");
        NOISE_OVERLAY = new Texture("src/group4/res/textures/noise-overlay.png");
        PRESS_ENTER = new Texture("src/group4/res/textures/press-enter.png");
        LOGO_FULLSCREEN = new Texture("src/group4/res/textures/logo-fullscreen.png");
        TOTEM_HELP = new Texture("src/group4/res/textures/totemhelp.png");
    }

    /**
     * Loads a texture located at path into openGL.
     * @param path relative path to the project root.
     * @return integer id for this texture in opengl
     */
    private int load(String path) {
        // read image from file and store contents in integer array
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            // get the RGB values of the whole image, read it into the pixel array. (offset 0, stride=width)
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* We need to change the format of the image data for opengl.
           We change it from ARGB to ABGR (because openGL reads from right to left).
         */
        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            // extract a,r,g,b layers from each pixel
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            // reorder it to ABGR
            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        // get a texture ID from opengl
        int result = glGenTextures();
        // bind the texture so we can change its parameters
        glBindTexture(GL_TEXTURE_2D, result);
        /*
        These two parameters prevent blurring: when we don't know the color of a pixel we take the value of the
        nearest pixel and we don't use linear interpolation (because that causes blurring).
         */
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // add our image data to the texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.getIntBuffer(data));
        // unbind the texture after setup is done
        glBindTexture(GL_TEXTURE_2D, 0);

        return result;
    }

    /**
     * This binds this texture to opengl. We can only use the texture when it is bound.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    /**
     * Unbinds this texture. Should be called after usage of the texture is over.
     */
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureID() {
        return texture;
    }

}
