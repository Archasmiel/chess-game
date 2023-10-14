package engine.scene;

import engine.shader.IShader;
import engine.shader.SimpleShader;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditScene extends Scene {

    private final LevelScene scene;

    private final float[] vertexArray = {
            // pos               // color
             0.5f, -0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,
            -0.5f,  0.5f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,
             0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 1.0f, 1.0f,
            -0.5f, -0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f,
    };

    // counter-clockwise order
    private final int[] elementArray = {
        2, 1, 0,  // top triangle
        0, 1, 3   // bot triangle
    };

    private int vaoID, vboID, eboID;
    private IShader defaultShader;

    public LevelEditScene(LevelScene scene) {
        this.scene = scene;
    }

    public void init() {
        defaultShader = new SimpleShader("assets/shaders/default.glsl");
        defaultShader.compile();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize*floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        defaultShader.use();
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detach();
    }
}
