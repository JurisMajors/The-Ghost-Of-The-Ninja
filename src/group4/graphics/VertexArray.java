package group4.graphics;

import group4.utils.BufferUtils;

import static org.lwjgl.opengl.GL41.*;


public class VertexArray {
    // The required buffers for drawing something in openGL
    private int vertexArrayObject;
    private int vertexBufferObject;
    private int indexBufferObject;
    private int texCoordBufferObject; // Optional if we would not use textures.

    private int count; // Number of elements the vertex array object should have.

    /**
     * Constructs a new VertexArray object which needs vertex positions, index orders for the vertex
     * positions, and finally texture coordinates for every vertex. Given this the VertexArray
     * will allow the caller to draw the buffers via the draw() method.
     *
     * @param vertices Float[], all the vertex positions
     * @param indices Byte[], the order in which vertices should be (re)-used. `indices.length` must be multiple of 3!
     * @param texCoords Float[], a uv/st-coordinate for every vertex.
     */
    public VertexArray(float[] vertices, byte[] indices, float[] texCoords) {
        count = indices.length;

        // VAO
        vertexArrayObject = glGenVertexArrays();
        glBindVertexArray(vertexArrayObject);

        // VBO
        vertexBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.getFloatBuffer(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.VERTEX_ATTRIBUTE, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIBUTE);

        // TexCoordBO
        texCoordBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, texCoordBufferObject);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.getFloatBuffer(texCoords), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.TEXCOORD_ATTRIBUTE, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.TEXCOORD_ATTRIBUTE);

        // IBO
        indexBufferObject = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.getByteBuffer(indices), GL_STATIC_DRAW);
    }

    private void bind() {
        glBindVertexArray(vertexArrayObject);
        // If the index buffer actually exists...
        if (indexBufferObject > 0) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
        }
    }

    private void draw() {
        if (indexBufferObject > 0) {
            glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
        } else {
            glDrawArrays(GL_TRIANGLES, 0, count);
        }
    }

    /**
     * Binds the buffers on the GPU and draws the elements on screen using GL_TRIANGLES according
     * to the buffer information.
     */
    public void render() {
        bind();
        draw();
    }

    public void deleteBuffers() {
//        System.out.println("deleting!");
        glDisableVertexAttribArray(0);
        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.vertexBufferObject);
        glDeleteBuffers(this.indexBufferObject);
        glDeleteBuffers(this.texCoordBufferObject);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(this.vertexArrayObject);


    }
}