package group4.game;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.ALC10.*;

/**
 * Class that facilitates audio usage in the game.
 */
public class Audio {

    /**
     * IDs for OpenAL.
     */
    public long audio; // openAL id for the audio
    public long audioContext; // openAL id for the audio context


    /**
     * Sets up the audio system.:w
     */
    public Audio() {
        init();
    }

    public void init() {
        // Initialize the audio
        String audioDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audio = alcOpenDevice(audioDeviceName);

        // no special attributes
        int[] attributes = {0};
        audioContext = alcCreateContext(audio, attributes);
        alcMakeContextCurrent(audioContext);

        // setup audio
        ALCCapabilities alcCapabilities = ALC.createCapabilities(audio);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    }

}
