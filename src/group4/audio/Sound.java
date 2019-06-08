package group4.audio;

import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

/**
 * Represents an audio file in the game.
 */
public class Sound {

    private final String audioPath = "src/group4/res/audio/";

    /**
     * Pointer to the buffer containing the audio.
     */
    private int sourcePointer;

    private static Random ran = new Random();

    /**
     * List of all sounds in the game.
     */
    public static Sound BACKGROUND;
    public static Sound BEEP;
    public static Sound[] SLASH;
    public static Sound SLASH1;
    public static Sound SLASH2;
    public static Sound STEP2;
    public static Sound STEP1;
    public static Sound[] STEP;
    public static Sound COIN;
    public static Sound DIE;
    public static Sound MOBHIT;
    public static Sound MOBDIE1;
    public static Sound MOBDIE2;
    public static Sound[] MOBDIE;
    public static Sound MENU;

    private static ArrayList<Sound> allSounds;

    /**
     * Initialize all sounds.
     */
    public static void loadAllSounds() {
        allSounds = new ArrayList<>();

        BACKGROUND = new Sound("background.ogg", true, 0.4f);
        BEEP = new Sound("fire.ogg", false);

        SLASH1 = new Sound("slash1.ogg", false);
        SLASH2 = new Sound("slash2.ogg", false);
        SLASH = new Sound[]{SLASH1, SLASH2};

        STEP1 = new Sound("step1.ogg", false);
        STEP2 = new Sound("step2.ogg", false);
        STEP = new Sound[]{STEP2, STEP1};

        COIN = new Sound("coin.ogg", false);

        DIE = new Sound("die.ogg", false, 0.8f);
        MOBDIE1 = new Sound("mobdie1.ogg", false, 0.7f);
        MOBDIE2 = new Sound("mobdie1.ogg", false, 0.7f);
        MOBDIE = new Sound[]{MOBDIE1, MOBDIE2};

        MOBHIT = new Sound("mobhit.ogg", false);

        MENU = new Sound("menu.ogg", false, 0.7f);

        allSounds.add(BACKGROUND);
        allSounds.add(BEEP);
        allSounds.add(SLASH1);
        allSounds.add(SLASH2);
        allSounds.add(STEP1);
        allSounds.add(STEP2);
        allSounds.add(COIN);
        allSounds.add(DIE);
        allSounds.add(MOBDIE1);
        allSounds.add(MOBDIE2);
        allSounds.add(MOBHIT);
        allSounds.add(MENU);
    }

    /**
     * Stops all sounds
     */
    public static void stopAllSounds() {
        for (Sound s : allSounds) {
            s.stop();
        }
    }

    /**
     * Create a sound from a .ogg file in path. If repeat is true the audio will keep repeating forever.
     * The volume can be set between 0.0f and 1.0f.
     *
     * @param path   path to source file.
     * @param repeat boolean
     * @param volume float between 0.0f and 1.0f
     */
    public Sound(String path, boolean repeat, float volume) {
        sourcePointer = load(path, repeat, volume);
    }

    /**
     * Alternative constructor.
     */
    public Sound(String path) {
        this(path, false);
    }

    /**
     * Alternative constructor.
     */
    public Sound(String path, boolean repeat) {
        this(path, repeat, -1);
    }

    /**
     * Plays this sound.
     */
    public void play() {
        //Play the sound
        alSourcePlay(sourcePointer);
    }

    public boolean isPlaying() {
        return AL_PLAYING == alGetSourcei(sourcePointer, AL_SOURCE_STATE);
    }

    public static boolean isPlaying(Sound... sounds) {
        for (Sound s : sounds) {
            if (s.isPlaying()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Play random sound from the given
     * @param sounds sounds to choose from
     */
    public static void playRandom(Sound... sounds) {
        sounds[ran.nextInt(sounds.length)].play();
    }

    /**
     * Stops this sound
     */
    public void stop() {
        // stop the sound
        alSourceStop(sourcePointer);
    }

    /**
     * Load a sound file with settings into the program.
     *
     * @param fileName name of the audio file (needs to be .ogg)
     * @param repeat   boolean
     * @param volume   gloat between 0.0f and 1.0f
     * @return
     */
    private int load(String fileName, boolean repeat, float volume) {
        String path = audioPath + fileName;


        // get one audio
        ShortBuffer rawAudioBuffer;

        int channels; // id for channels
        int sampleRate; // id for sample rate

        try (MemoryStack stack = stackPush()) {
            //Allocate space to store return information from the function
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);

            rawAudioBuffer = stb_vorbis_decode_filename(path, channelsBuffer, sampleRateBuffer);

            // Retreive the extra information that was stored in the buffers by the function
            channels = channelsBuffer.get(0);
            sampleRate = sampleRateBuffer.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        //Find the correct OpenAL format
        int format = -1;
        if (channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if (channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        //Request space for the buffer
        int bufferPointer = alGenBuffers();

        //Send the data to OpenAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);

        //Free the memory allocated by STB
        free(rawAudioBuffer);

        //Request a source
        int sourcePointer = alGenSources();

        //Assign the sound we just loaded to the source
        alSourcei(sourcePointer, AL_BUFFER, bufferPointer);

        // set repeating
        if (repeat) {
            alSourcei(sourcePointer, AL_LOOPING, 1);
        }

        // set volume
        if (volume != -1) {
            alSourcef(sourcePointer, AL_GAIN, volume);
        }

        return sourcePointer;
    }


}
