package group4.maths;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.components.identities.GhostComponent;
import group4.ECS.components.identities.MobComponent;
import group4.ECS.components.identities.PlayerComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.etc.Mappers;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Ray {
    /**
     * starting position of the ray
     **/
    Vector3f startPos;
    /**
     * direction of the ray
     **/
    Vector3f dir;
    /**
     * the end of the ray
     **/
    public Vector3f end;
    /**
     * If an entity contains a component in this list, let the rays go through it
     */
    private List<Class<? extends Component>> ignorableComponents;

    public Ray(Vector3f startingPos, Vector3f dir, List<Class<? extends Component>> ignorableComponents, float length) {
        this.startPos = startingPos;
        this.dir = dir;
        this.end = this.startPos.add(this.dir.normalized().scale(length));
        this.ignorableComponents = ignorableComponents;
    }

    public Ray(Vector3f startingPos, Vector3f dir) {
        List<Class<? extends Component>> defaultList = new ArrayList<>();
        defaultList.add(GhostComponent.class);
        defaultList.add(PlayerComponent.class);

        this.startPos = startingPos;
        this.dir = dir;
        this.end = this.startPos.add(this.dir.normalized().scale(10000));
        this.ignorableComponents = defaultList;
    }

    /**
     * Cast the ray to the given entities
     *
     * @param entities the entities that the ray might intersect
     * @return position at first intersection of a bounding box
     */
    public IntersectionPair cast(ImmutableArray<Entity> entities) {
        Vector3f closestIntersection = null; // current closest intersection of the ray
        Entity intersectedEntity = null; // entity whose bounding box is intersected
        float curDist = Float.MAX_VALUE; // the distance to the intersection

        // for each entity calculate the intersection
        entity_loop:
        for (Entity e : entities) {
            // ignore entities that contain an ignoreable component
            for (Class<? extends Component> component: this.ignorableComponents) {
                // skip this entity
                if (e.getComponent(component) != null) {
                    continue entity_loop;
                }
            }

            List<Vector3f> intersections = this.intersects(e); // get intersection points

            // get minimal intersection point and update if necessary
            for (Vector3f inter : intersections) {
                // calculate intersections distance
                float interDist = inter.euclidDist(this.startPos);
                // update the closest intersection and distance if necessary;
                if (interDist < curDist) {
                    curDist = interDist;
                    closestIntersection = inter;
                    intersectedEntity = e;
                }
            }
        }
        if (closestIntersection == null) {
            closestIntersection = this.end;
        }
        return new IntersectionPair(closestIntersection, intersectedEntity);
    }

    /**
     * Ray intersection point with a single entity
     *
     * @param entity the entity to check for an intersection
     * @return the positions of intersections with the  bounding box of the entity
     */
    public List<Vector3f> intersects(Entity entity) {
        List<Vector3f> intersections = new ArrayList<>();
        PositionComponent posComp = Mappers.positionMapper.get(entity);
        DimensionComponent dimComp = Mappers.dimensionMapper.get(entity);
        Vector3f dim = dimComp.dimension;
        Vector3f botL = posComp.position;

        // points of the bounding box
        // the z coordinates dont matter, since we only care about 2D intersection
        Vector3f botLeft = new Vector3f(botL); // copy botleft
        Vector3f topLeft = new Vector3f(botLeft.x, botLeft.y + dim.y, botLeft.z);
        Vector3f topRight = botLeft.add(dim);
        Vector3f botRight = new Vector3f(botLeft.x + dim.x, botLeft.y, botLeft.z);

        // define lines of the bounding box
        List<Line2D.Float> lines = new ArrayList<>();
        lines.add(createLine(topLeft, topRight));
        lines.add(createLine(topLeft, botLeft));
        lines.add(createLine(botLeft, botRight));
        lines.add(createLine(botRight, topRight));
        // this can be extended for more dimensions if necessary...
        Line2D.Float rayAsLine = createLine(this.startPos, this.end);
        // check for intersections on each line
        for (Line2D line : lines) {
            // if line segments dont intersect, skip
            if (!line.intersectsLine(rayAsLine)) continue;
            // otherwise calculate the intersection between the "infinite lines"
            Vector3f inter = this.lineIntersection(new Vector3f((float) line.getX1(), (float) line.getY1(), 0),
                    new Vector3f((float) line.getX2(), (float) line.getY2(), 0));
            if (inter == null) {
                continue;
            }
            intersections.add(inter);
        }

        return intersections;
    }

    private Line2D.Float createLine(Vector3f a, Vector3f b) {
        return new Line2D.Float(a.x, a.y,
                b.x, b.y);
    }

    private Vector3f lineIntersection(Vector3f a, Vector3f b) {
        // assumes infinite lines
        // https://gamedev.stackexchange.com/questions/111100/intersection-of-a-line-and-a-rectangle
        float A1 = this.end.y - this.startPos.y;
        float B1 = this.startPos.x - this.end.x;

        float A2 = a.y - b.y;
        float B2 = b.x - a.x;

        float delta = A1 * B2 - A2 * B1;
        if (delta < 1e-6 && delta > -1 * 1e-6) {
            return null;
        }

        float C2 = A2 * b.x + B2 * b.y;
        float C1 = A1 * this.startPos.x + B1 * this.startPos.y;

        float invdelta = 1 / delta;
        return new Vector3f((B2 * C1 - B1 * C2) * invdelta, (A1 * C2 - A2 * C1) * invdelta, this.startPos.z);
    }
}
