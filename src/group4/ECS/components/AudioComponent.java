package group4.ECS.components;

import com.badlogic.ashley.core.Component;

import java.io.File;

public class AudioComponent implements Component {

    // don't create new file, refer to existing audio file
    public File misc = null;
    public File collision = null;
    public File attack = null;
    public File death = null;
    public File move = null;
    public File jump = null;

}
