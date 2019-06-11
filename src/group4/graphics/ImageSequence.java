package group4.graphics;

import group4.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageSequence {
    public static ImageSequence COIN;
    public static ImageSequence TORCH;
    public static ImageSequence TORCH_LIGHT;
    public static ImageSequence BAT;
    public static ImageSequence BAT2;

    public List<Texture> frames;
    public int frameCount;

    public ImageSequence(String folder) {
        this.frames = load(folder);
        this.frameCount = this.frames.size();
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
        TORCH = new ImageSequence("src/group4/res/textures/animated/torch/");
        TORCH_LIGHT = new ImageSequence("src/group4/res/textures/animated/torchlight/");
        BAT = new ImageSequence("src/group4/res/textures/animated/bat/");
        BAT2 = new ImageSequence("src/group4/res/textures/animated/bat2/");
    }
}
