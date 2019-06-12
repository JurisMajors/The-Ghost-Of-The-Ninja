package group4.ECS.systems.GraphHandlers;

import com.badlogic.ashley.core.Entity;
import com.google.common.graph.Graph;
import group4.ECS.components.GraphComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.GravityComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.AStarMobs.AStarMob;
import group4.ECS.entities.world.Platform;
import group4.ECS.entities.world.SplinePlatform;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public abstract class AbstractGraphHandler<T extends AStarMob> {


    public void constructGraph(Entity entity, GraphComponent gc) {
        if (gc.vertexCoords == null) {
            if (gc.inFile != null) {
                try {
                    readFromFile(entity, gc.inFile);
                } catch (IOException e) {
                }
            } else {
                generateVertices(entity);
                computeYHValues(entity);
                if (gc.outFile != null) {
                    try {
                        writeInFile(entity, gc.outFile);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    public void readFromFile(Entity entity, String inFile) throws IOException {
        GraphComponent gc = Mappers.graphMapper.get(entity);
        Scanner sc = new Scanner(new File(inFile));

        int vertices = sc.nextInt();
        gc.vertexCoords = new ArrayList<Vector3f>();
        for (int i = 0; i < vertices; i++) gc.vertexCoords.add(new Vector3f(sc.nextFloat(), sc.nextFloat(), 0.0f));

        gc.neighbours = new ArrayList<ArrayList<Integer>>();
        gc.edgeID = new int[vertices][vertices];
        for (int i = 0; i < vertices; i++) {
            int neighbours = sc.nextInt();
            gc.neighbours.add(new ArrayList<Integer>());
            for (int j = 0; j < neighbours; j++) {
                int neighbour = sc.nextInt();
                gc.neighbours.get(i).add(neighbour);
                gc.edgeID[i][neighbour] = sc.nextInt();
            }
        }

        int edges = sc.nextInt();
        gc.edgePath = new ArrayList<ArrayList<Vector3f>>();
        for (int i = 0; i < edges; i++) {
            int length = sc.nextInt();
            gc.edgePath.add(new ArrayList<Vector3f>());
            for (int j = 0; j < length; j++) gc.edgePath.get(i).add(new Vector3f(sc.nextFloat(), sc.nextFloat(), 0.0f));
        }


        int values = sc.nextInt();
        gc.yHValue = new TreeMap<Float, Integer>();
        for (int i = 0; i < values; i++) gc.yHValue.put(sc.nextFloat(), sc.nextInt());

        sc.close();
    }

    public void writeInFile(Entity entity, String outFile) throws IOException {
        GraphComponent gc = Mappers.graphMapper.get(entity);
        FileWriter fw = new FileWriter(outFile);

        fw.write(gc.vertexCoords.size() + " ");
        for (int i = 0; i < gc.vertexCoords.size(); i++)
            fw.write(gc.vertexCoords.get(i).x + " " + gc.vertexCoords.get(i).y + " ");

        for (int i = 0; i < gc.vertexCoords.size(); i++) {
            fw.write(gc.neighbours.get(i).size() + " ");
            for (int j = 0; j < gc.neighbours.get(i).size(); j++)
                fw.write(gc.neighbours.get(i).get(j) + " " + gc.edgeID[i][gc.neighbours.get(i).get(j)] + " ");
        }

        fw.write(gc.edgePath.size() + " ");
        for (int i = 0; i < gc.edgePath.size(); i++) {
            fw.write(gc.edgePath.get(i).size() + " ");
            for (int j = 0; j < gc.edgePath.get(i).size(); j++)
                fw.write(gc.edgePath.get(i).get(j).x + " " + gc.edgePath.get(i).get(j).y + " ");
        }

        fw.write(gc.yHValue.size() + " ");
        float key = 0.0f;
        for (int i = 0; i < gc.yHValue.size(); i++) {
            if (i == 0) key = gc.yHValue.firstKey();
            else key = gc.yHValue.higherKey(key);
            fw.write(key + " " + gc.yHValue.get(key) + " ");
        }

        fw.close();
    }

    public void addCollisionPair(Entity entity, TreeMap<Float, Float> collisionPairs, float lp, float hp) {
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
        GraphComponent gc = Mappers.graphMapper.get(entity);
        if (lp - dc.dimension.y >= 0 && lp <= gc.module.getHeight() && hp >= 0 && hp + dc.dimension.y <= gc.module.getHeight() && lp <= hp) {
            float value = hp;
            if (collisionPairs.containsKey(lp)) {
                value = Math.max(hp, collisionPairs.get(lp));
                collisionPairs.remove(lp);
            }
            collisionPairs.put(lp, value);
        }
    }

    public void computeCollisionPairs(Entity entity, float xCoord, TreeMap<Float, Float> collisionPairs, ArrayList<Platform> platforms, ArrayList<SplinePlatform> splines) {
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);

        for (int j = 0; j < platforms.size(); j++) {
            float lp = platforms.get(j).lowestPoint(xCoord, xCoord + dc.dimension.x);
            float hp = platforms.get(j).highestPoint(xCoord, xCoord + dc.dimension.x);
            addCollisionPair(entity, collisionPairs, lp, hp);
        }
        for (int j = 0; j < splines.size(); j++) {
            float lp = splines.get(j).lowestPoint(xCoord, xCoord + dc.dimension.x);
            float hp = splines.get(j).highestPoint(xCoord, xCoord + dc.dimension.x);
            addCollisionPair(entity, collisionPairs, lp, hp);
        }
    }

    public void computeCollisionSystem(Entity entity, float xCoord, TreeMap<Float, Float> collisionPairs, HashMap<Float, TreeMap<Float, Integer>> bottomCollision, HashMap<Float, ArrayList<Float>> highestTopCollision) {
        if (collisionPairs.size() > 0) {
            bottomCollision.put(xCoord, new TreeMap<Float, Integer>());
            highestTopCollision.put(xCoord, new ArrayList<Float>());

            float key = -1.0f;
            float maxValue = -1.0f;
            for (int i = 0; i < collisionPairs.size(); i++) {
                key = collisionPairs.higherKey(key);
                maxValue = Math.max(maxValue, collisionPairs.get(key));
                bottomCollision.get(xCoord).put(key, i);
                highestTopCollision.get(xCoord).add(maxValue);
            }
        }
    }

    public boolean checkPosition(Entity entity, float xCoord, float bottom, float top, HashMap<Float, TreeMap<Float, Integer>> bottomCollision, HashMap<Float, ArrayList<Float>> highestTopCollision) {
        GraphComponent gc = Mappers.graphMapper.get(entity);
        if (bottom < 0 || top > gc.module.getHeight() || xCoord < 0 || xCoord > gc.module.getWidth()) return false;
        if (!bottomCollision.containsKey(xCoord)) return true;
        if (bottomCollision.get(xCoord).size() == 0) return true;
        if (bottomCollision.get(xCoord).firstKey() >= top) return true;
        if (highestTopCollision.get(xCoord).get(bottomCollision.get(xCoord).get(bottomCollision.get(xCoord).lowerKey(top))) <= bottom)
            return true;
        return false;
    }

    public void addVertex(Entity entity, float xCoord, float yCoord, HashMap<Float, TreeMap<Float, Integer>> coordsVertex, HashMap<Float, HashMap<Float, Integer>> hashCoordsVertex) {
        GraphComponent gc = Mappers.graphMapper.get(entity);
        if (!hashCoordsVertex.containsKey(xCoord)) {
            hashCoordsVertex.put(xCoord, new HashMap<Float, Integer>());
            coordsVertex.put(xCoord, new TreeMap<Float, Integer>());
        }
        if (!hashCoordsVertex.get(xCoord).containsKey(yCoord)) {
            hashCoordsVertex.get(xCoord).put(yCoord, gc.vertexCoords.size());
            coordsVertex.get(xCoord).put(yCoord, gc.vertexCoords.size());
            gc.vertexCoords.add(new Vector3f(xCoord, yCoord, 0.0f));
            gc.neighbours.add(new ArrayList<Integer>());
        }
    }

    public void addCeiling(float xCoord, float yCoord, HashMap<Float, TreeSet<Float>> ceiling) {
        if (!ceiling.containsKey(xCoord)) ceiling.put(xCoord, new TreeSet<Float>());
        ceiling.get(xCoord).add(yCoord);
    }


    protected void generateVertices(Entity entity) {
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GraphComponent gc = Mappers.graphMapper.get(entity);

        Iterable<Entity> entities = gc.module.getEntities();
        ArrayList<Platform> platforms = new ArrayList<Platform>();
        ArrayList<SplinePlatform> splines = new ArrayList<SplinePlatform>();
        for (Entity ent : entities) {
            if (ent instanceof Platform) platforms.add((Platform) ent);
            else if (ent instanceof SplinePlatform) splines.add((SplinePlatform) ent);
        }

        HashMap<Float, TreeMap<Float, Integer>> bottomCollision = new HashMap<Float, TreeMap<Float, Integer>>();
        HashMap<Float, ArrayList<Float>> highestTopCollision = new HashMap<Float, ArrayList<Float>>();
        HashMap<Float, TreeMap<Float, Integer>> coordsVertex = new HashMap<Float, TreeMap<Float, Integer>>();
        HashMap<Float, HashMap<Float, Integer>> hashCoordsVertex = new HashMap<Float, HashMap<Float, Integer>>();
        HashMap<Float, TreeSet<Float>> ceiling = new HashMap<Float, TreeSet<Float>>();
        gc.vertexCoords = new ArrayList<Vector3f>();
        gc.neighbours = new ArrayList<ArrayList<Integer>>();

        for (int j = 0; mc.velocityRange.x * (float) j <= gc.module.getWidth(); j++) {
            float xCoord = mc.velocityRange.x * (float) j;
            TreeMap<Float, Float> collisionPairs = new TreeMap<Float, Float>();
            computeCollisionPairs(entity, xCoord, collisionPairs, platforms, splines);
            computeCollisionSystem(entity, xCoord, collisionPairs, bottomCollision, highestTopCollision);

            float key = -1.0f;
            for (int i = 0; i < collisionPairs.size(); i++) {
                key = collisionPairs.higherKey(key);
                float value = collisionPairs.get(key);
                if (checkPosition(entity, xCoord, value, value + dc.dimension.y, bottomCollision, highestTopCollision)) {
                    addVertex(entity, xCoord, value, coordsVertex, hashCoordsVertex);
                }
                if (checkPosition(entity, xCoord, key - dc.dimension.y, key, bottomCollision, highestTopCollision)) {
                    addCeiling(xCoord, key - dc.dimension.y, ceiling);
                }
            }
        }

        computeEdges(entity, coordsVertex, hashCoordsVertex, ceiling, bottomCollision, highestTopCollision);
    }

    public float adjustX(Entity entity, float x) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        int i = (int) (x / mc.velocityRange.x);
        float x1 = mc.velocityRange.x * (float) i;
        i++;
        float x2 = mc.velocityRange.x * (float) i;
        if (x - x1 <= x2 - x) return x1;
        return x2;
    }

    public void leftNode(Entity entity, ArrayList<Node> currLayer, Node parent) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent g = Mappers.gravityMapper.get(entity);
        Vector3f coordinates = parent.coordinates.add(new Vector3f(-mc.velocityRange.x, parent.yVelocity - g.gravity.y, 0.0f));
        coordinates.x = adjustX(entity, coordinates.x);
        currLayer.add(new Node(parent, parent.movements + 1, parent.yVelocity - g.gravity.y, coordinates));
    }

    public void rightNode(Entity entity, ArrayList<Node> currLayer, Node parent) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent g = Mappers.gravityMapper.get(entity);
        Vector3f coordinates = parent.coordinates.add(new Vector3f(mc.velocityRange.x, parent.yVelocity - g.gravity.y, 0.0f));
        coordinates.x = adjustX(entity, coordinates.x);
        currLayer.add(new Node(parent, parent.movements + 1, parent.yVelocity - g.gravity.y, coordinates));
    }

    public void leftUpNode(Entity entity, ArrayList<Node> currLayer, Node parent) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent g = Mappers.gravityMapper.get(entity);
        Vector3f coordinates = parent.coordinates.add(new Vector3f(-mc.velocityRange.x, parent.yVelocity - g.gravity.y + mc.velocityRange.y, 0.0f));
        coordinates.x = adjustX(entity, coordinates.x);
        currLayer.add(new Node(parent, parent.movements + 1, parent.yVelocity - g.gravity.y + mc.velocityRange.y, coordinates));
    }

    public void rightUpNode(Entity entity, ArrayList<Node> currLayer, Node parent) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent g = Mappers.gravityMapper.get(entity);
        Vector3f coordinates = parent.coordinates.add(new Vector3f(mc.velocityRange.x, parent.yVelocity - g.gravity.y + mc.velocityRange.y, 0.0f));
        coordinates.x = adjustX(entity, coordinates.x);
        currLayer.add(new Node(parent, parent.movements + 1, parent.yVelocity - g.gravity.y + mc.velocityRange.y, coordinates));
    }

    public void upNode(Entity entity, ArrayList<Node> currLayer, Node parent) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent g = Mappers.gravityMapper.get(entity);
        Vector3f coordinates = parent.coordinates.add(new Vector3f(0.0f, parent.yVelocity - g.gravity.y + mc.velocityRange.y, 0.0f));
        coordinates.x = adjustX(entity, coordinates.x);
        currLayer.add(new Node(parent, parent.movements, parent.yVelocity - g.gravity.y + mc.velocityRange.y, coordinates));
    }

    public void stayNode(Entity entity, ArrayList<Node> currLayer, Node parent) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GravityComponent g = Mappers.gravityMapper.get(entity);
        Vector3f coordinates = parent.coordinates.add(new Vector3f(0.0f, parent.yVelocity - g.gravity.y, 0.0f));
        coordinates.x = adjustX(entity, coordinates.x);
        currLayer.add(new Node(parent, parent.movements, parent.yVelocity - g.gravity.y, coordinates));
    }

    protected void generateFirstLayerNodes(Entity entity, ArrayList<Node> prevLayer, ArrayList<Node> currLayer) {
        for (int i = 0; i < prevLayer.size(); i++) {
            leftNode(entity, currLayer, prevLayer.get(i));
            rightNode(entity, currLayer, prevLayer.get(i));
            leftUpNode(entity, currLayer, prevLayer.get(i));
            rightUpNode(entity, currLayer, prevLayer.get(i));
            upNode(entity, currLayer, prevLayer.get(i));
        }
    }

    protected void generateNodes(Entity entity, ArrayList<Node> prevLayer, ArrayList<Node> currLayer) {
        for (int i = 0; i < prevLayer.size(); i++) {
            leftNode(entity, currLayer, prevLayer.get(i));
            rightNode(entity, currLayer, prevLayer.get(i));
            stayNode(entity, currLayer, prevLayer.get(i));
        }
    }

    public void adjustYPos(Entity entity, Node node, HashMap<Float, TreeSet<Float>> ceiling) {
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
        GraphComponent gc = Mappers.graphMapper.get(entity);

        if (ceiling.containsKey(node.coordinates.x) && ceiling.get(node.coordinates.x).size() > 0 && ceiling.get(node.coordinates.x).first() <= node.coordinates.y) {
            if (node.coordinates.y - ceiling.get(node.coordinates.x).floor(node.coordinates.y) <= node.yVelocity) {
                node.coordinates.y = ceiling.get(node.coordinates.x).floor(node.coordinates.y);
                node.yVelocity = 0.0f;
            }
        } else if (node.coordinates.y + dc.dimension.y > gc.module.getHeight()) {
            node.coordinates.y = gc.module.getHeight() - dc.dimension.y;
            node.yVelocity = 0.0f;
        }
    }

    public void adjustYNeg(Node node, HashMap<Float, TreeMap<Float, Integer>> coordsVertex) {
        if (coordsVertex.containsKey(node.coordinates.x) && coordsVertex.get(node.coordinates.x).size() > 0 && coordsVertex.get(node.coordinates.x).lastKey() >= node.coordinates.y
                && coordsVertex.get(node.coordinates.x).ceilingKey(node.coordinates.y) - node.coordinates.y <= -node.yVelocity) {
            node.coordinates.y = coordsVertex.get(node.coordinates.x).ceilingKey(node.coordinates.y);
            node.yVelocity = 0.0f;
        }
    }

    public void removeCollidingFirstLayerNodes(Entity entity, ArrayList<Node> currLayer, HashMap<Float, TreeMap<Float, Integer>> coordsVertex, HashMap<Float, TreeSet<Float>> ceiling, HashMap<Float, TreeMap<Float, Integer>> bottomCollision, HashMap<Float, ArrayList<Float>> highestTopCollision) {
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);
        GravityComponent g = Mappers.gravityMapper.get(entity);

        ArrayList<Node> tempLayer = new ArrayList<Node>();
        for (int i = 0; i < currLayer.size(); i++) {
            if (currLayer.get(i).yVelocity > 0.0f) adjustYPos(entity, currLayer.get(i), ceiling);
            else if (currLayer.get(i).yVelocity < 0.0f) {
                if (currLayer.get(i).yVelocity == -g.gravity.y) {
                    currLayer.get(i).yVelocity -= 0.5f;
                    adjustYNeg(currLayer.get(i), coordsVertex);
                    if (currLayer.get(i).yVelocity != 0.0f) currLayer.get(i).yVelocity += 0.5f;
                } else adjustYNeg(currLayer.get(i), coordsVertex);
            }
            if (checkPosition(entity, currLayer.get(i).coordinates.x, currLayer.get(i).coordinates.y, currLayer.get(i).coordinates.y + dc.dimension.y, bottomCollision, highestTopCollision)) {
                tempLayer.add(currLayer.get(i));
            }
        }
        currLayer.clear();
        currLayer.addAll(tempLayer);
    }

    public void removeCollidingNodes(Entity entity, ArrayList<Node> currLayer, HashMap<Float, TreeMap<Float, Integer>> coordsVertex, HashMap<Float, TreeSet<Float>> ceiling, HashMap<Float, TreeMap<Float, Integer>> bottomCollision, HashMap<Float, ArrayList<Float>> highestTopCollision) {
        DimensionComponent dc = Mappers.dimensionMapper.get(entity);

        ArrayList<Node> tempLayer = new ArrayList<Node>();
        for (int i = 0; i < currLayer.size(); i++) {
            if (currLayer.get(i).yVelocity > 0.0f) adjustYPos(entity, currLayer.get(i), ceiling);
            else if (currLayer.get(i).yVelocity < 0.0f) adjustYNeg(currLayer.get(i), coordsVertex);
            if (checkPosition(entity, currLayer.get(i).coordinates.x, currLayer.get(i).coordinates.y, currLayer.get(i).coordinates.y + dc.dimension.y, bottomCollision, highestTopCollision)) {
                tempLayer.add(currLayer.get(i));
            }
        }
        currLayer.clear();
        currLayer.addAll(tempLayer);
    }

    public void removeDuplicateNodes(ArrayList<Node> currLayer) {
        ArrayList<Node> tempLayer = new ArrayList<Node>();
        HashMap<Float, HashMap<Float, HashMap<Float, Integer>>> layer = new HashMap<Float, HashMap<Float, HashMap<Float, Integer>>>();
        for (int i = 0; i < currLayer.size(); i++) {
            if (!layer.containsKey(currLayer.get(i).coordinates.x)) {
                layer.put(currLayer.get(i).coordinates.x, new HashMap<Float, HashMap<Float, Integer>>());
            }
            if (!layer.get(currLayer.get(i).coordinates.x).containsKey(currLayer.get(i).coordinates.y)) {
                layer.get(currLayer.get(i).coordinates.x).put(currLayer.get(i).coordinates.y, new HashMap<Float, Integer>());
            }
            if (!layer.get(currLayer.get(i).coordinates.x).get(currLayer.get(i).coordinates.y).containsKey(currLayer.get(i).yVelocity)) {
                layer.get(currLayer.get(i).coordinates.x).get(currLayer.get(i).coordinates.y).put(currLayer.get(i).yVelocity, tempLayer.size());
                tempLayer.add(currLayer.get(i));
            } else {
                int j = layer.get(currLayer.get(i).coordinates.x).get(currLayer.get(i).coordinates.y).get(currLayer.get(i).yVelocity);
                if (tempLayer.get(j).movements > currLayer.get(i).movements) {
                    tempLayer.set(j, currLayer.get(i));
                }
            }
        }
        currLayer.clear();
        currLayer.addAll(tempLayer);
    }

    public void removeFinalNodes(Entity entity, int initVertexID, ArrayList<Node> currLayer, HashMap<Float, HashMap<Float, Integer>> hashCoordsVertex, HashSet<Integer> finished) {
        GraphComponent gc = Mappers.graphMapper.get(entity);

        ArrayList<Node> tempLayer = new ArrayList<Node>();
        for (int i = 0; i < currLayer.size(); i++) {
            if (hashCoordsVertex.containsKey(currLayer.get(i).coordinates.x) && hashCoordsVertex.get(currLayer.get(i).coordinates.x).containsKey(currLayer.get(i).coordinates.y)) {
                int vertexID = hashCoordsVertex.get(currLayer.get(i).coordinates.x).get(currLayer.get(i).coordinates.y);
                if (!finished.contains(vertexID)) {
                    ArrayList<Vector3f> reversedPath = new ArrayList<Vector3f>();
                    Node node = currLayer.get(i);
                    while (node != null) {
                        reversedPath.add(node.coordinates);
                        node = node.parent;
                    }
                    gc.edgeID[initVertexID][vertexID] = gc.edgePath.size();
                    gc.neighbours.get(initVertexID).add(vertexID);
                    finished.add(vertexID);

                    gc.edgePath.add(new ArrayList<Vector3f>());
                    for (int j = reversedPath.size() - 1; j >= 0; j--)
                        gc.edgePath.get(gc.edgePath.size() - 1).add(reversedPath.get(j));
                }
            } else tempLayer.add(currLayer.get(i));
        }
        currLayer.clear();
        currLayer.addAll(tempLayer);
    }

    public void buildLayer(Entity entity, int initVertexID, ArrayList<Node> prevLayer, ArrayList<Node> currLayer, HashSet<Integer> finished, Vector3f position, HashMap<Float, TreeMap<Float, Integer>> coordsVertex, HashMap<Float, HashMap<Float, Integer>> hashCoordsVertex, HashMap<Float, TreeSet<Float>> ceiling, HashMap<Float, TreeMap<Float, Integer>> bottomCollision, HashMap<Float, ArrayList<Float>> highestTopCollision) {
        if (prevLayer.size() == 0) {
            prevLayer.add(new Node(null, 0, 0.0f, position));
            generateFirstLayerNodes(entity, prevLayer, currLayer);
            removeCollidingFirstLayerNodes(entity, currLayer, coordsVertex, ceiling, bottomCollision, highestTopCollision);
            for (int i = 0; i < currLayer.size(); i++) currLayer.get(i).parent = null;
        } else {
            generateNodes(entity, prevLayer, currLayer);
            removeCollidingNodes(entity, currLayer, coordsVertex, ceiling, bottomCollision, highestTopCollision);
        }
        removeDuplicateNodes(currLayer);
        removeFinalNodes(entity, initVertexID, currLayer, hashCoordsVertex, finished);
    }

    class Node {
        public Node parent;
        public int movements;
        public float yVelocity;
        public Vector3f coordinates;

        public Node(Node parent, int movements, float yVelocity, Vector3f coordinates) {
            this.parent = parent;
            this.movements = movements;
            this.yVelocity = yVelocity;
            this.coordinates = coordinates;
        }
    }

    public void computeEdges(Entity entity, HashMap<Float, TreeMap<Float, Integer>> coordsVertex, HashMap<Float, HashMap<Float, Integer>> hashCoordsVertex, HashMap<Float, TreeSet<Float>> ceiling, HashMap<Float, TreeMap<Float, Integer>> bottomCollision, HashMap<Float, ArrayList<Float>> highestTopCollision) {
        GraphComponent gc = Mappers.graphMapper.get(entity);

        gc.edgePath = new ArrayList<ArrayList<Vector3f>>();
        gc.edgeID = new int[gc.vertexCoords.size()][gc.vertexCoords.size()];

        for (int z = 0; z < gc.vertexCoords.size(); z++) {
            ArrayList<Node> prevLayer = new ArrayList<Node>();
            ArrayList<Node> currLayer = new ArrayList<Node>();
            HashSet<Integer> finished = new HashSet<Integer>();
            finished.add(z);
            buildLayer(entity, z, prevLayer, currLayer, finished, gc.vertexCoords.get(z), coordsVertex, hashCoordsVertex, ceiling, bottomCollision, highestTopCollision);
            prevLayer = currLayer;
            currLayer = new ArrayList<Node>();
            while (prevLayer.size() > 0) {
                buildLayer(entity, z, prevLayer, currLayer, finished, gc.vertexCoords.get(z), coordsVertex, hashCoordsVertex, ceiling, bottomCollision, highestTopCollision);
                prevLayer = currLayer;
                currLayer = new ArrayList<Node>();
                ;
            }
        }
    }

    protected void posYHValues(Entity entity) {
        MovementComponent mc = Mappers.movementMapper.get(entity);
        GraphComponent gc = Mappers.graphMapper.get(entity);
        GravityComponent g = Mappers.gravityMapper.get(entity);

        float position = 0.0f;
        float velocity = mc.velocityRange.y - g.gravity.y;
        ArrayList<Float> heights = new ArrayList<Float>();

        while (velocity > 0) {
            position += velocity;
            heights.add(position);
            velocity -= g.gravity.y;
        }
        int size = heights.size();
        while (heights.get(heights.size() - 1) <= gc.module.getHeight()) {
            int ssize = heights.size();
            for (int i = 0; i < size && heights.get(heights.size() - 1) <= gc.module.getHeight(); i++)
                heights.add(heights.get(i) + heights.get(ssize - 1));
        }
        for (int i = 0; i < heights.size(); i++) gc.yHValue.put(heights.get(i), i + 1);
    }

    protected void negYHValues(Entity entity) {
        GraphComponent gc = Mappers.graphMapper.get(entity);
        GravityComponent g = Mappers.gravityMapper.get(entity);

        float position = 0.0f;
        float velocity = -g.gravity.y;
        ArrayList<Float> heights = new ArrayList<Float>();

        while (heights.size() == 0 || -heights.get(heights.size() - 1) < gc.module.getHeight()) {
            position += velocity;
            heights.add(position);
            velocity -= g.gravity.y;
        }
        for (int i = 0; i < heights.size(); i++) gc.yHValue.put(heights.get(i), i + 1);
    }

    public void computeYHValues(Entity entity) {
        GraphComponent gc = Mappers.graphMapper.get(entity);

        gc.yHValue = new TreeMap<Float, Integer>();
        posYHValues(entity);
        gc.yHValue.put(0.0f, 0);
        negYHValues(entity);
    }
}
