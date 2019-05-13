package group4.utils;

import group4.maths.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class DebugUtils {
    private static float lineWidth = 2.0f;
    private static Vector3f color = new Vector3f(1.0f, 0.0f, 0.0f); // RED

    // nobody should create an object of this class
    private DebugUtils() {
    }

    ;

    /**
     * Given two vector positions, draws a red line between them using old style OpenGL.
     *
     * @param a Vector3f, starting point of the line segment to draw
     * @param b Vector3f, ending point of the line segment to draw
     */
    public static void drawLine(Vector3f a, Vector3f b) {
        glLineWidth(lineWidth);
        glColor3f(1.0f, 0.0f, 0.0f);
        glBegin(GL_LINES);
        glVertex2f(a.x, a.y);
        glVertex2f(b.x, b.y);
        glEnd();
    }

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
}
