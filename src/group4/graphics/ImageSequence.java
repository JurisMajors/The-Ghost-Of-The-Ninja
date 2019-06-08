package group4.graphics;

import group4.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageSequence {
    public static ImageSequence COIN;

    List<Texture> frames;
    public ImageSequence(String folder) {
        frames = load(folder);
    }

    private List<Texture> load(String folder) {
        List<Texture> frames = new ArrayList<>();
        List<String> files = FileUtils.getFilePaths(folder);
        for (String f : files) {
            frames.add(new Texture(f));
        }
        return frames;
    }


    /**
     * Function which loads in all image sequences(texture arrays) as constants for easy access later on.
     * Should be executed once during game initialization.
     */
    public static void loadAllImageSequences() {
        COIN = new ImageSequence("src/group4/res/textures/animated/coin/");
    }
}
