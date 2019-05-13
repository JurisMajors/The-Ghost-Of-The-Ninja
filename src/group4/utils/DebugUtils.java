package group4.utils;

import group4.maths.Matrix4f;
import group4.maths.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class DebugUtils {
    // nobody should create an object of this class
    private DebugUtils(){};

    public static void drawLine(Matrix4f mat, Vector3f a, Vector3f b) {
//        glPushMatrix();
//        glLoadMatrixf(mat.toFloatBuffer());
        glLineWidth(2.5f);
        glColor3f(1.0f, 0.0f, 0.0f);
        glBegin(GL_LINES);
        glVertex2f(a.x, a.y);
        glVertex2f(b.x, b.y);
        glEnd();
//        glPopMatrix();
    }
}
