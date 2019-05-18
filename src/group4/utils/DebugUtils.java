package group4.utils;

import group4.ECS.entities.world.SplinePlatform;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;
import group4.maths.spline.CubicBezierSpline;
import group4.maths.spline.MultiSpline;
import group4.maths.spline.Spline;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class DebugUtils {
    private static float lineWidth = 2.0f;
    private static Vector3f color = new Vector3f(1.0f, 0.0f, 0.0f); // RED

    // nobody should create an object of this class
    private DebugUtils() {
    }

    /**
     * Given two vector positions, draws a red line between them using old style OpenGL.
     *
     * @param a Vector3f, starting point of the line segment to draw
     * @param b Vector3f, ending point of the line segment to draw
     */
    public static void drawLine(Vector3f a, Vector3f b) {
        glLineWidth(lineWidth);
        glColor3f(color.x, color.y, color.z);
        glBegin(GL_LINES);
        glVertex2f(a.x, a.y);
        glVertex2f(b.x, b.y);
        glEnd();
    }

    /**
     * Given two opposite corner positions, draws a red lined bbox between them using old style OpenGL.
     *
     * @param bottomLeft Vector3f, first corner of the bbox to draw
     * @param topRight   Vector3f, second corner of the bbox to draw
     */
    public static void drawBox(Vector3f bottomLeft, Vector3f topRight) {
        glLineWidth(lineWidth);
        glColor3f(color.x, color.y, color.z);
        glBegin(GL_LINE_STRIP);
        glVertex2f(bottomLeft.x, bottomLeft.y);
        glVertex2f(topRight.x, bottomLeft.y);
        glVertex2f(topRight.x, topRight.y);
        glVertex2f(bottomLeft.x, topRight.y);
        glEnd();
    }

    /**
     * Draws a grid where the grid cells are square and of the given cellSize. Utilizes
     * the drawLine function for drawing large vertical and horizontal lines.
     *
     * @param cellSize Float, the length of the side a square grid cell should be
     */
    public static void drawGrid(float cellSize) {
        for (int i = -100; i < 100; i++) {
            // Draw vertical
            drawLine(new Vector3f(cellSize * i, -1000.0f, 0.0f),
                    new Vector3f(cellSize * i, 1000.0f, 0.0f));

            // Draw horizontal
            drawLine(new Vector3f(-1000.0f, cellSize * i, 0.0f),
                    new Vector3f(1000.0f, cellSize * i, 0.0f));
        }
    }

    /**
     * Draws a circle at the given center position with the specified radius. The circle will be approximated with line
     * segments. Many segments => smooth circle.
     *
     * @param center   Vector3f, the center position of the circle
     * @param radius   Float, the radius of the circle
     * @param segments Integer, how many segments to use to form the circle
     */
    public static void drawCircle(Vector3f center, float radius, int segments) {
        double step = 360.0f / segments;
        double angle = 0.0;
        List<Vector3f> points = new ArrayList<>();
        for (int i = 0; i < segments; i++) {
            points.add(
                    new Vector3f(
                            (float) Math.asin(angle) * radius + center.x,
                            (float) Math.acos(angle) * radius + center.y,
                            center.z
                    )
            );
            angle += step;
        }
        
    }

    public static void drawSpline() {
        // my little spline test
        Vector3f[] tempPoint = new Vector3f[]{
                new Vector3f(),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 1.0f, 0.0f),
                new Vector3f(2.0f, 1.0f, 0.0f)
        };
        for (Vector3f v : tempPoint) {
            v.addi(new Vector3f(5.0f, 5.0f, 0.0f));
        }

        MultiSpline mySpline = new MultiSpline(tempPoint);

        int detail = 100;
        float dt = 1 / (float) detail;
        float t = 0.0f;

        Vector3f[] points = new Vector3f[detail + 1];
        Vector3f[] normals = new Vector3f[detail + 1];
        for (int i = 0; i <= detail; i++) {
            points[i] = mySpline.getPoint(t);
            normals[i] = mySpline.getNormal(t);
            t += dt;
        }
        for (int i = 1; i <= detail; i++) {
            drawLine(points[i - 1], points[i]);
        }
        for (int i = 0; i <= detail; i++) {
            drawLine(points[i], points[i].add(normals[i]));
        }


    }
}
