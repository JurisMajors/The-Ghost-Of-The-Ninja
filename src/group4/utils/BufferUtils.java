package group4.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


public class BufferUtils {

    // nobody should create an object of this class
    private BufferUtils() {
    }

    /**
     * Create a byte buffer from a byte array.
     * @param array
     * @return byte buffer
     */
    public static ByteBuffer getByteBuffer(byte[] array) {
        // create byte buffer from byte array
        ByteBuffer buffer = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
        // flip the buffer to make openGL happy
        buffer.put(array).flip();

        return buffer;
    }

    /**
     * Create a float buffer from a float array.
     * @param array
     * @return float buffer
     */
    public static FloatBuffer getFloatBuffer(float[] array) {
        // one float needs 4 bytes, hence array.length << 2
        FloatBuffer buffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(array).flip();

        return buffer;
    }

    /**
     * Create an int buffer from an int array.
     * @param array
     * @return int buffer
     */
    public static IntBuffer getIntBuffer(int[] array) {
        // one int needs 4 bytes, hence array.length << 2
        IntBuffer buffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(array).flip();

        return buffer;
    }

}
