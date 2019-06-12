package group4.levelSystem;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphComponent;
import group4.ECS.entities.AStarMobs.AStarMob;
import group4.ECS.entities.AStarMobs.JumpingAStarMob;
import group4.ECS.entities.AStarMobs.JumpingWalkingAStarMob;
import group4.ECS.entities.AStarMobs.WalkingAStarMob;
import group4.ECS.entities.Player;
import group4.ECS.entities.hazards.Spikes;
import group4.ECS.entities.items.consumables.Coin;
import group4.ECS.entities.mobs.FlyingMob;
import group4.ECS.entities.mobs.Mob;
import group4.ECS.entities.totems.EndingTotem;
import group4.ECS.entities.totems.StartTotem;
import group4.ECS.entities.totems.Totem;
import group4.ECS.entities.world.*;
import group4.ECS.etc.TheEngine;
import group4.ECS.systems.GraphHandlers.AbstractGraphHandler;
import group4.ECS.systems.GraphHandlers.JumpingAStarMobGraphHandler;
import group4.ECS.systems.GraphHandlers.JumpingWalkingAStarMobGraphHandler;
import group4.ECS.systems.GraphHandlers.WalkingAStarMobGraphHandler;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // ghost directory
    private String ghostModelDir;

    // Keeps track of the initial player position
    private Vector3f initialPlayerPos;

    // JSON Object containing the tiled data
    JSONObject tiledData;

    // Storing the mapping from the tilemap indices to the engine Entities
    private Map<Integer, String> moduleTileMap;
    // maps character of spline to its control points
    private Map<Character, List<Vector3f>> splineMap;

    /**
     * Graphs for AStar mobs to not recompute again
     */
    GraphComponent jwGraph = new GraphComponent(this); // jumping walking
    GraphComponent wGraph = new GraphComponent(this); // walking
    GraphComponent jGraph = new GraphComponent(this); // jumping graph
    /**
     * A star handlers for all different types of mobs.
     */
    public static AbstractGraphHandler jwHandler = new JumpingWalkingAStarMobGraphHandler();
    public static AbstractGraphHandler wHandler = new WalkingAStarMobGraphHandler();
    public static AbstractGraphHandler jHandler = new JumpingAStarMobGraphHandler();

    List<Mob> mobs = new ArrayList<>();

    /**
     * Default construct, which constructs a module based on a Tiled .tmx file
     */

    public Module(Level l, String tiledModuleLocation, String ghostModelDir) {
        if (ghostModelDir != null) {
            loadGhost(ghostModelDir);
        }
        this.configureMap();
        this.splineMap = new HashMap<>();
        this.loadTiledObject(tiledModuleLocation);
        this.setup(l);
    }


    /**
     * Constructor to work with non-tiled modules
     */
    public Module(Level l, String ghostModelDir) {
        if (ghostModelDir != null) {
            loadGhost(ghostModelDir);
        }
        this.setup(l);
    }



    private void loadGhost(String loc) {
        this.ghostModelDir = loc;
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
    }


    /**
     * This method is used to reset the module to its initial state
     */
    public void reset() {
        this.setup(this.level);
        this.level.configExits();
    }


    /**
     * Populates @code{this.entities} with default entities for the module
     */
    protected void constructModule() {
        // First, we set the height and width of the module
        this.height = this.tiledData.getInt("height");
        this.width = this.tiledData.getInt("width");

        // Now, we get all tile layers from the JSON object
        JSONArray layers = this.tiledData.getJSONArray("layers");

        // Now, we loop over the layers
        for (int i = 0; i < layers.length(); i++) {
            JSONObject layer = layers.getJSONObject(i);
            String layerName = layer.getString("name");

            if (layerName.equals("MAIN")) {
                parseMainLayer(layer);
            } else if (layerName.equals("SPLINES")) {
                parseSplineLayer(layer);
            } else if (layerName.equals("COINS")) {
                if (Main.AI) continue;
                parseCoinLayer(layer);
            } else if (layerName.equals("TOTEMS")) {
                if (Main.AI) continue;
                parseTotemLayer(layer);
            } else if (layerName.equals("EXITS")) {
                setupExits(layer);
            } else if (layerName.equals("LIGHTS")) {
                parseLightLayer(layer);
            } else {
                // In case the layer is not MAIN or EXITS
                System.err.println("Unrecognized layer name: " + layer.getString("name"));
                setupExits(layer);
            }
        }
    }

    private void parseCoinLayer(JSONObject layer) {
        // Get height and width of layer
        int layerHeight = layer.getInt("height");
        int layerWidth = layer.getInt("width");

        // Loop over the data grid
        JSONArray data = layer.getJSONArray("data");
        for (int tile = 0; tile < data.length(); tile++) {
            // Get the grid position of the tile
            int tileGridX = tile % layerWidth;
            int tileGridY = layerHeight - tile / layerWidth;

            // Get the type of the tile
            int tileId = data.getInt(tile) - 1;

            String entityId = moduleTileMap.get(tileId);

            if (entityId == null) {
                continue;
            } else if (entityId.equals(Coin.getName())) {
                addCoin(new Vector3f(tileGridX, tileGridY, 0), tileId);
            } else {
                System.err.println("Some tiles not drawing!");
                continue;
            }
        }

    }

    private void parseLightLayer(JSONObject layer) {
        // Get height and width of layer
        int layerHeight = layer.getInt("height");
        int layerWidth = layer.getInt("width");

        // Loop over the data grid
        JSONArray data = layer.getJSONArray("data");
        for (int tile = 0; tile < data.length(); tile++) {
            // Get the grid position of the tile
            int tileGridX = tile % layerWidth;
            int tileGridY = layerHeight - tile / layerWidth;

            // Get the type of the tile
            int tileId = data.getInt(tile) - 1;
            String entityId = moduleTileMap.get(tileId);
            if (entityId == null) {
                continue;
            } else if (entityId.equals(Torch.getName())) {
                addTorch(new Vector3f(tileGridX, tileGridY, 0), tileId);
            } else {
                System.err.println("Some tiles not drawing!");
                continue;
            }
        }
    }

    private void parseMainLayer(JSONObject layer) {
        // Get height and width of layer
        int layerHeight = layer.getInt("height");
        int layerWidth = layer.getInt("width");

        // Loop over the data grid
        JSONArray data = layer.getJSONArray("data");
        for (int tile = 0; tile < data.length(); tile++) {
            // Get the grid position of the tile
            int tileGridX = tile % layerWidth;
            int tileGridY = layerHeight - tile / layerWidth;

            // Get the type of the tile
            int tileId = data.getInt(tile) - 1;

            String entityId = moduleTileMap.get(tileId);

            if (entityId == null) {
                continue;
            } else if (entityId.equals(Platform.getName())) {
                this.addPlatform(tileGridX, tileGridY, tileId);
            } else if (entityId.equals(ArtTile.getName())) {
                this.addArtTile(tileGridX, tileGridY, tileId);
            } else if (entityId.equals(Player.getName())) {
                this.initialPlayerPos = new Vector3f(tileGridX, tileGridY, 0.0f);
            } else if (entityId.endsWith(Mob.getName())) {
                if (Main.AI) continue;
                this.addMob(tileGridX, tileGridY, tileId, entityId);
            } else if (entityId.equals(Spikes.getName())) {
                this.addSpike(tileGridX, tileGridY, tileId);
            } else {
                System.err.println("Some tiles not drawing!");
                continue;
            }
        }
    }

    private void addCoin(Vector3f position, int i) {
        Coin c = new Coin(position, Coin.LARGE_SIZE, Shader.SIMPLE, Texture.MAIN_TILES, TileMapping.MAIN.get(i), Coin.LARGE_VALUE);
        this.addEntity(c);
    }

    private void addTorch(Vector3f position, int i) {
        Torch torch = new Torch(position);
        TorchLight torchLight = new TorchLight(position);
        this.addEntity(torch);
        this.addEntity(torchLight);
    }

    private void parseTotemLayer(JSONObject layer) {
        JSONArray data = layer.getJSONArray("objects");
        for (int point = 0; point < data.length(); point++) {
            // get information about the object
            JSONObject pointInfo = data.getJSONObject(point);
            // get the coordinates for the control point
            float pointX = pointInfo.getFloat("x") / 32f;
            float pointY = this.height - pointInfo.getFloat("y") / 32f + 1;
            String tileName = pointInfo.getString("name");
            Vector3f position = new Vector3f(pointX, pointY, 0);
            Totem totem;
            if (Totem.isEnd(tileName)) {
                totem = new EndingTotem(position, tileName, level);
            } else {
                totem = new StartTotem(position, tileName, level, this.ghostModelDir);
            }
            this.addEntity(totem);
        }
    }

    private void parseSplineLayer(JSONObject layer) {
        // only load in the splines if they are not loaded yet
        if (splineMap.isEmpty()) {
            // Loop over the data grid
            JSONArray data = layer.getJSONArray("objects");
            for (int point = 0; point < data.length(); point++) {
                // get information about the object
                JSONObject pointInfo = data.getJSONObject(point);
                // get the coordinates for the control point
                float pointX = pointInfo.getFloat("x") / 32f;
                float pointY = this.height - pointInfo.getFloat("y") / 32f + 1;

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
        }

        // add splines from our cached spline map
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
        SplinePlatform platform = new SplinePlatform(spline, Shader.SIMPLE, Texture.SPLINE);
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
        // Set the player's spawned ghost to false, as the ghost was also automatically removed
        this.getLevel().getPlayer().spawnedGhost = false;
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
     * Adds an artTile entity to the module. These entities only render. They have no collision.
     *
     * @param x the x position of the platform in the module grid
     * @param y the y position of the platform in the module grid
     * @param i the identifier for the tile within the TileMap
     */
    private void addArtTile(int x, int y, int i) {
        Vector3f tempPosition = new Vector3f(x, y, 0.0f);
        Vector3f tempDimension = new Vector3f(1.0f, 1.0f, 0.0f);
        ArtTile p = new ArtTile(tempPosition, tempDimension, Shader.SIMPLE, Texture.MAIN_TILES, TileMapping.MAIN.get(i));
        this.addEntity(p);
    }

    private void addMob(int x, int y, int i, String mobName) {
        Vector3f tempPosition = new Vector3f(x, y, 0.0f);
        Mob m = null;
        if (mobName.equals(JumpingWalkingAStarMob.getName())) {
            m = new JumpingWalkingAStarMob(tempPosition, this.level, this, Texture.MAIN_TILES, TileMapping.MAIN.get(i), jwGraph);
        } else if (mobName.equals(WalkingAStarMob.getName())) {
            m = new WalkingAStarMob(tempPosition, this.level, this, Texture.MAIN_TILES, TileMapping.MAIN.get(i), wGraph);
        } else if (mobName.equals(JumpingAStarMob.getName())) {
            m = new JumpingAStarMob(tempPosition, this.level, this, Texture.MAIN_TILES, TileMapping.MAIN.get(i), jGraph);
        } else if (mobName.equals(FlyingMob.getName())) {
            m = new FlyingMob(tempPosition, this.level);
        }
        if (m == null) return;
        if (!(m instanceof FlyingMob)) {
            this.mobs.add(m);
        }
        this.addEntity(m);
    }

    public void finalizeMobs() {
        for (Mob m : this.mobs) {
            if (m instanceof JumpingAStarMob) {
                jHandler.constructGraph(m,jGraph);
            } else if (m instanceof WalkingAStarMob) {
                wHandler.constructGraph(m, wGraph);
            } else if (m instanceof JumpingWalkingAStarMob) {
                jwHandler.constructGraph(m, jwGraph);
            }
        }
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
     * Adds an artTile entity to the module. These entities only render. They have no collision.
     *
     * @param x the x position of the platform in the module grid
     * @param y the y position of the platform in the module grid
     * @param i the identifier for the tile within the TileMap
     */
    private void addSpike(int x, int y, int i) {
        Vector3f tempPosition = new Vector3f(x, y, 0.0f);
        Spikes p;

        switch (i) {
            case 48:
                p = new Spikes(tempPosition, Shader.SIMPLE, Texture.MAIN_TILES, TileMapping.MAIN.get(i),
                        new Vector3f(0.0f, 1.0f, 0.0f));
                break;
            case 49:
                p = new Spikes(tempPosition, Shader.SIMPLE, Texture.MAIN_TILES, TileMapping.MAIN.get(i),
                        new Vector3f(1.0f, 0.0f, 0.0f));
                break;
            case 50:
                p = new Spikes(tempPosition, Shader.SIMPLE, Texture.MAIN_TILES, TileMapping.MAIN.get(i),
                        new Vector3f(0.0f, -1.0f, 0.0f));
                break;
            case 51:
                p = new Spikes(tempPosition, Shader.SIMPLE, Texture.MAIN_TILES, TileMapping.MAIN.get(i),
                        new Vector3f(-1.0f, 0.0f, 0.0f));
                break;
            default:
                p = new Spikes(tempPosition, Shader.SIMPLE, Texture.MAIN_TILES, TileMapping.MAIN.get(i),
                        new Vector3f(0.0f, 1.0f, 0.0f));
                break;
        }

        this.addEntity(p);
    }

    /**
     * Given the data for the "EXITS" layer, adds all exits and gives them an integer ID for the module
     * to which they point.
     *
     * @param exitLayer JSONObject, the layer within the "tiled" JSON file.
     */
    private void setupExits(JSONObject exitLayer) {
        // Loop over the data grid
        JSONArray data = exitLayer.getJSONArray("data");
        for (int tile = 0; tile < data.length(); tile++) {
            // Get height and width of layer
            int layerHeight = exitLayer.getInt("height");
            int layerWidth = exitLayer.getInt("width");

            // Get the grid position of the tile
            int tileGridX = tile % layerWidth;
            int tileGridY = layerHeight - tile / layerWidth;

            // Get the type of the tile
            int targetModule = data.getInt(tile); // NOTE: targetModule == tileId in the tilemap texture here as well
            if (targetModule == 0) {
                continue; // Default "no tile placed here" marker is 0.
            } else {
                /*
                    We subtract TileMapping.MAIN_SIZE here from targetModule, as the tilenumber is based off the second
                    "exits" tilemap, and hence its number is offset by the size of the first "main" tilemap.

                                ...Beware, dark magic around these parts...

                    Consider the following:
                        int targetModule = data.getInt(tile) - some_number;  Causes targetModule to be "- some_number".

                    Similarly for:
                        int targetModule = data.getInt(tile);
                        targetModule = targetModule - some_number; (or targetModule -= some_number)

                    The following line below this comment works however as expected...
                 */
                addExit(tileGridX, tileGridY, targetModule - TileMapping.MAIN_SIZE);
            }
        }
    }

    /**
     * Adds a exit entity to the module
     *
     * @param x            the x position of the exit in the module grid
     * @param y            the y position of the exit in the module grid
     * @param targetModule the integer identifier for the module (with respect to the level) to which the exit points
     */
    private void addExit(int x, int y, int targetModule) {
        Vector3f tempPosition = new Vector3f(x, y, 0.0f);
        Vector3f tempDimension = new Vector3f(2.0f, 2.0f, 0.0f);
        Exit e = new Exit(tempPosition, tempDimension, this, targetModule);
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
        int[] artTiles = new int[]{3, 4, 11, 12};
        int[] players = new int[]{7};
        int spike_up = 48;
        int spike_right = 49;
        int spike_down = 50;
        int spike_left = 51;
        int jumpingwalkingmob = 36;
        int jumpingmob = 42;
        int walkingmob = 40;
        int flyingmob = 41;
        int coin = 13;
        int torch = 15;
        int totemStart = 21;
        int totemEnd = 29;

        moduleTileMap = new HashMap<Integer, String>();
        for (int i : platforms) {
            moduleTileMap.put(i, Platform.getName());
        }

        for (int i : artTiles) {
            moduleTileMap.put(i, ArtTile.getName());
        }

        for (int i : players) {
            moduleTileMap.put(i, Player.getName());
        }

        // spikes
        moduleTileMap.put(spike_up, Spikes.getName());
        moduleTileMap.put(spike_right, Spikes.getName());
        moduleTileMap.put(spike_down, Spikes.getName());
        moduleTileMap.put(spike_left, Spikes.getName());

        moduleTileMap.put(jumpingwalkingmob, JumpingWalkingAStarMob.getName());
        moduleTileMap.put(jumpingmob, JumpingAStarMob.getName());
        moduleTileMap.put(walkingmob, WalkingAStarMob.getName());
        moduleTileMap.put(flyingmob, FlyingMob.getName());

        moduleTileMap.put(coin, Coin.getName());
        moduleTileMap.put(torch, Torch.getName());
        moduleTileMap.put(totemStart, StartTotem.getName());
        moduleTileMap.put(totemEnd, EndingTotem.getName());
    }
}
