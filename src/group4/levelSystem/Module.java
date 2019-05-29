package group4.levelSystem;

import com.badlogic.ashley.core.Entity;
import group4.AI.Brain;
import group4.AI.Evolver;
import group4.ECS.entities.Camera;
import group4.ECS.entities.Ghost;
import group4.ECS.entities.Player;
import group4.ECS.entities.world.Exit;
import group4.ECS.entities.world.Platform;
import group4.ECS.entities.world.SplinePlatform;
import group4.ECS.etc.TheEngine;
import group4.game.Main;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.TileMapping;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * This class defines the interface for modules that can be used to create levels
 * A module represents a height * width grid on which entities can be positioned
 * entities may overlap multiple cells of the grid
 * A module can have entries and exits that can link to other modules in a levelSystem (links defined on levelSystem basis)
 */
public class Module {

    // Define the size of the module grid
    // All modules should be of the same size to keep things simple
    protected int height;
    protected int width;

    // Keep track of the level that this module instance is part of
    private Level level;

    // List that keeps track of all the entities in the module
    private List<Entity> entities;

    // ghost model
    private String ghostPath = null;
    private Brain ghostModel = null;

    // Keeps track of the initial player position
    private Vector3f initialPlayerPos;

    // JSON Object containing the tiled data
    JSONObject tiledData;

    // Storing the mapping from the tilemap indices to the engine Entities
    private Map<Integer, String> moduleTileMap;
    // maps character of spline to its control points
    private Map<Character, List<Vector3f>> splineMap;


    /**
     * Default construct, which constructs a module based on a Tiled .tmx file
     */

    public Module(Level l, String TiledModuleLocation, String ghostModelName) {
        if (ghostModelName != null) {
            this.ghostPath = Evolver.path + ghostModelName;
        }
        this.configureMap();
        this.splineMap = new HashMap<>();
        this.loadTiledObject(TiledModuleLocation);
        this.setup(l);
    }


    /**
     * Constructor to work with non-tiled modules
     */
    public Module(Level l, String ghostModelName) {
        if (ghostModelName != null) {
            this.ghostPath = Evolver.path + ghostModelName;
        }
        this.setup(l);
    }


    /**
     * This method is used to create a JSON object containing the tiled module information
     */
    private void loadTiledObject(String fileLocation) {
        // Try to read the tiled json file
        try {
            FileReader fileReader = new FileReader(fileLocation);

            // Construct the JSON object containing the tiled module information
            this.tiledData = new JSONObject(new JSONTokener(fileReader));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Module: could not find the tiled module JSON file");
        }
    }


    /**
     * This method is used to set up the module in its initial state or reset the module to its initial state
     */
    private final void setup(Level l) {
        if (l == null) throw new IllegalArgumentException("Module: Level cannot be null");
        this.level = l;
        this.entities = new ArrayList<>();
        this.constructModule();
        this.ghostModel = new Brain(this.ghostPath);
    }


    /**
     * This method is used to reset the module to its initial state
     */
    public void reset() {
        this.setup(this.level);
    }


    /**
     * Populates @code{this.entities} with default entities for the module
     */
    protected void constructModule() {
        // TODO: This is a bad spot for this, but it demonstrates the functionality. Please move.
        Camera camera = new Camera();
        this.addEntity(camera); // Adding the camera to the module (which adds it to the engine?)

        // First, we set the height and width of the module
        this.height = this.tiledData.getInt("height");
        this.width = this.tiledData.getInt("width");

        // Now, we get all tile layers from the JSON object
        JSONArray layers = this.tiledData.getJSONArray("layers");

        // Now, we loop over the layers
        for (int i = 0; i < layers.length(); i++) {
            JSONObject layer = layers.getJSONObject(i);
            String layerName = layer.getString("name");
            // Get height and width of layer
            if (layerName.equals("MAIN")) {
                parseMainLayer(layer);
            } else if (layerName.equals("SPLINES")) {
                parseSplineLayer(layer);
            }
        }
    }

    private void parseMainLayer(JSONObject layer) {
        int layerHeight = layer.getInt("height");
        int layerWidth = layer.getInt("width");

        // Loop over the data grid
        JSONArray data = layer.getJSONArray("data");
        for (int tile = 0; tile < data.length(); tile++) {
            // Get the grid position of the tile
            int tileGridX = tile % layerWidth;
            int tileGridY = layerHeight - 1 - (int) Math.floor(tile / layerWidth);

            // Get the type of the tile
            int tileId = Integer.parseInt(data.get(tile).toString()) - 1;

            String entityId = moduleTileMap.get(tileId);

            // TODO: Can't use switch with static function as comparison. i.e. case Platform.getName() is not possible. Something better?
            if (entityId == null) {
                continue;
            } else if (entityId.equals(Platform.getName())) {
                this.addPlatform(tileGridX, tileGridY, tileId);
            } else if (entityId.equals(Exit.getName())) {
                this.addExit(tileGridX, tileGridY, tileId);
            } else if (entityId.equals(Player.getName())) {
                this.initialPlayerPos = new Vector3f(tileGridX, tileGridY, 0.0f);
            } else {
                continue;
            }
        }
    }

    private void parseSplineLayer(JSONObject layer) {
        // Loop over the data grid
        JSONArray data = layer.getJSONArray("objects");
        for (int point = 0; point < data.length(); point++) {
            // get information about the object
            JSONObject pointInfo = data.getJSONObject(point);
            // get the coordinates for the control point
            float pointX = pointInfo.getFloat("x") / 32f;
            float pointY = this.height - pointInfo.getFloat("y") / 32f;

            String tileName = pointInfo.getString("name");
            // get the identification of the spline (first character in the string)
            char splineId = tileName.charAt(0);
            // get the identification of the current point within the spline
            int pointId = Integer.parseInt(tileName.substring(1));

            if (!splineMap.containsKey(splineId)) { // create a new spline array if none exists for this spline
                splineMap.put(splineId, new ArrayList<>(data.length()));
            }
            // add the control point to the spline
            List<Vector3f> points = splineMap.get(splineId);
            if (pointId >= points.size()) {
                points.add(new Vector3f(pointX, pointY, 0));
            } else {
                points.add(pointId, new Vector3f(pointX, pointY, 0));
            }
        }

        for (List<Vector3f> cPoints : splineMap.values()) { // for each given control point
            addSpline(cPoints);
        }
    }

    /**
     * Adds a spline to the module with the given control points
     *
     * @param cPoints control points of the spline
     * @throws IllegalStateException if cPoints.size() % 4 != 0
     */
    private void addSpline(List<Vector3f> cPoints) throws IllegalStateException {
        if (cPoints.size() % 4 != 0) throw new IllegalStateException("Module.addSpline() " +
                "Control points must be multiple of 4, " +
                "was given " + cPoints.size() + " control points instead!");
        Vector3f[] cPointsArr = cPoints.toArray(new Vector3f[0]);
        MultiSpline spline = new MultiSpline(cPointsArr);
        SplinePlatform platform = new SplinePlatform(spline, Shader.SIMPLE, Texture.WHITE);
        this.addEntity(platform);
    }


    /**
     * Return the initial position of the player in the module
     */
    public Vector3f getPlayerInitialPosition() {
        return new Vector3f(this.initialPlayerPos);
    }


    /**
     * Load the current module into the engine, so it gets rendered
     */
    public final void load() {
        for (Entity e : this.entities) {
            TheEngine.getInstance().addEntity(e);
        }
    }


    /**
     * Unload the current module from the engine, to stop it from being rendered
     */
    public final void unload() {
        for (Entity e : this.entities) {
            TheEngine.getInstance().removeEntity(e);
        }
    }


    /**
     * Add an entity to the module
     *
     * @param e the entity to add to the module
     */
    public void addEntity(Entity e) {
        this.entities.add(e);
    }


    /**
     * Remove an entity from the module
     *
     * @param e the entity to remove from the module
     */
    public void removeEntity(Entity e) {
        this.entities.remove(e);
    }


    /**
     * Get an iterator of the entities that are in this module
     */
    public Iterable<Entity> getEntities() {
        return this.entities;
    }


    /**
     * Add a ghost to the current module
     */
    public void addGhost(Vector3f position) throws IllegalStateException {
        if (Main.AI) return;

        if (this.entities == null) {
            throw new IllegalStateException("Adding ghost before initialized entities container");
        }
        if (this.ghostPath != null) {
            TheEngine.getInstance().addEntity(new Ghost(position,this.level, this.ghostModel));
        } else {
            System.err.println("WARNING: Not loading ghost in module");
        }
    }


    /**
     * Get the width of the module grid
     */
    public int getWidth() {
        return this.width;
    }


    /**
     * Get the height of the module grid
     */
    public int getHeight() {
        return this.height;
    }


    /**
     * Adds a platform entity to the module
     *
     * @param x the x position of the platform in the module grid
     * @param y the y position of the platform in the module grid
     * @param i the identifier for the tile within the TileMap
     */
    private void addPlatform(int x, int y, int i) {
        Vector3f tempPosition = new Vector3f(x, y, 0.0f);
        Vector3f tempDimension = new Vector3f(1.0f, 1.0f, 0.0f);
        Platform p = new Platform(tempPosition, tempDimension, Shader.SIMPLE, Texture.MAIN_TILES, TileMapping.MAIN.get(i));
        this.addEntity(p);
    }

    /**
     * Adds a exit entity to the module
     *
     * @param x the x position of the exit in the module grid
     * @param y the y position of the exit in the module grid
     * @param i the identifier for the tile within the TileMap
     */
    private void addExit(int x, int y, int i) {
        Vector3f tempPosition = new Vector3f(x, y, 0.0f);
        Vector3f tempDimension = new Vector3f(1.0f, 1.0f, 0.0f);
        Exit e = new Exit(tempPosition, tempDimension, this, TileMapping.MAIN.get(i));
        this.addEntity(e);
    }


    /**
     * Get all exits of this module
     */
    public List<Exit> getExits() {
        List<Exit> exits = new ArrayList<>();

        for (Entity e : this.entities) {
            if (e instanceof Exit) {
                exits.add((Exit) e);
            }
        }

        return exits;
    }


    /**
     * Get the level that this module is part of
     */
    public Level getLevel() {
        return this.level;
    }

    /**
     * Function which contains the mapping from the tileMap index as on the texture, to the appropriate Entity class.
     */
    private void configureMap() {
        int[] platforms = new int[]{0, 1, 2, 5, 6, 8, 9, 10, 16, 17, 18, 19, 20, 24, 25, 26, 27, 28, 32, 33, 34, 35};
        int[] exits = new int[]{3, 4, 11, 12};
        int[] players = new int[]{7};
        moduleTileMap = new HashMap<Integer, String>();
        for (int i : platforms) {
            moduleTileMap.put(i, Platform.getName());
        }

        for (int i : exits) {
            moduleTileMap.put(i, Exit.getName());
        }

        for (int i : players) {
            moduleTileMap.put(i, Player.getName());
        }
    }
}
