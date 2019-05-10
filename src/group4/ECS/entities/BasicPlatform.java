package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PositionComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.graphics.VertexArray;
import group4.maths.Vector3f;
import group4.utils.ShaderParser;

import java.util.ArrayList;

public class BasicPlatform extends Entity {

    Vector3f position;
    Vector3f size;

    public BasicPlatform(Vector3f position, Vector3f size, Texture texture, boolean collidable) {
        this.position = position;
        this.size = size;

        Vector3f bl = blPosition();
        int texWidth = texture.getWidth();
        int texHeight = texture.getHeight();

        float endY = bl.add(size).y;
        float endX = bl.add(size).x;

        ArrayList<Vector3f> vertexArray = new ArrayList<>();
        ArrayList<Vector3f> tcArray = new ArrayList<>();
        ArrayList<Byte> indices = new ArrayList<>();

        byte startI = 0;
        // create texture blocks over the platform
        for (int x = 0; x <= endX; x += texWidth) {
            for (int y = 0; y <= endY; y += texHeight) {
                float thisEndY = Math.min(endY, y + texHeight);
                float thisEndX = Math.min(endX, x + texWidth);
                // add a texture block starting at (x,y)
                // bottomleft
                Vector3f blVertex = new Vector3f(x, y, 0.0f);
                Vector3f blTc = new Vector3f(0, 0, 0);
                // bottomright
                Vector3f brVertex = new Vector3f(x + thisEndX, y, 0.0f);
                Vector3f brTc = new Vector3f((thisEndX - x) / texWidth, 0, 0);
                // topright
                Vector3f trVertex = new Vector3f(x + thisEndX, y + thisEndY, 0.0f);
                Vector3f trTc = new Vector3f((thisEndX - x) / texWidth, (thisEndY - y) / texHeight, 0);
                // topleft
                Vector3f tlVertex = new Vector3f(x, y + thisEndY, 0.0f);
                Vector3f tlTc = new Vector3f(x, (thisEndY - y) / texHeight, 0);

                // add the vertices and texture coordinates
                vertexArray.add(blVertex);
                vertexArray.add(brVertex);
                vertexArray.add(trVertex);
                vertexArray.add(tlVertex);
                tcArray.add(blTc);
                tcArray.add(brTc);
                tcArray.add(trTc);
                tcArray.add(tlTc);
                // first triangle
                indices.add(startI);
                indices.add((byte) (startI + 1));
                indices.add((byte) (startI + 2));
                // second triangle
                indices.add((byte) (startI + 2));
                indices.add((byte) (startI + 3));
                indices.add(startI);

                startI += 4;
            }
        }

        // turn the arraylists into arrays for the VertexArray object
        float[] va = createVertexArray(vertexArray);
        float[] tc = createTextureArray(tcArray);
        byte[] ic = createIndicesArray(indices);

//        VertexArray vertexArrayObject = new VertexArray(va, ic, tc);

//        Shader s = ShaderParser.loadShader("src/group4/res/shaders/simple/");
//        Texture t = new Texture("src/group4/res/textures/debug.jpeg");

        Entity add = this.add(new PositionComponent(position));
        this.add(new GraphicsComponent("src/group4/res/shaders/simple/", "src/group4/res/textures/brick.png", va, ic, tc));
//        GraphicsComponent(String shader, String texture, float[] vertices, byte[] indices, float[] tcs){
        }

        /**
         * Calculates and returns the bottom left position of this platform.
         *
         * @return bottom left player
         */
        private Vector3f blPosition () {
            return position.sub(size.scale(0.5f));
        }

        private float[] createVertexArray (ArrayList < Vector3f > al) {
            float[] result = new float[al.size() * 3];
            // add the x y z as a separate element
            for (int i = 0; i < al.size(); i++) {
                result[3 * i + 0] = al.get(i).x;
                result[3 * i + 1] = al.get(i).y;
                result[3 * i + 2] = al.get(i).z;
            }
            return result;
        }

        private float[] createTextureArray (ArrayList < Vector3f > al) {
            float[] result = new float[al.size() * 3];
            // add the x y z as a separate element
            for (int i = 0; i < al.size(); i++) {
                result[2 * i + 0] = al.get(i).x;
                result[2 * i + 1] = al.get(i).y;
            }
            return result;
        }

        private byte[] createIndicesArray (ArrayList < Byte > al) {
            byte[] result = new byte[al.size()];
            for (int i = 0; i < al.size(); i++) {
                result[i] = al.get(i);
            }
            return result;
        }
    }
