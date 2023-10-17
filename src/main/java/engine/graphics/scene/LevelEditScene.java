package engine.graphics.scene;

import engine.graphics.camera.Camera;
import engine.graphics.shader.Shader;
import engine.graphics.texture.Texture;
import engine.utility.TimeMgmt;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditScene extends Scene {

    private final LevelScene scene;
    private final float mult = 100f;
    private static final int positionSize = 3;
    private static final int colorSize = 4;
    private static final int uvSize = 2;
    private final float[] vertexArray = {
            // pos               // color
             1f*mult, 0f*mult, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,   1, 0,
             0f*mult, 1f*mult, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,   0, 1,
             1f*mult, 1f*mult, 0.0f,   1.0f, 0.0f, 1.0f, 1.0f,   1, 1,
             0f*mult, 0f*mult, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f,   0, 0,
    };

    // counter-clockwise order
    private final int[] elementArray = {
        2, 1, 0,  // top triangle
        0, 1, 3   // bot triangle
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;

    public LevelEditScene(LevelScene scene) {
        this.scene = scene;
    }

    public void init() {
        this.camera = new Camera(new Vector2f(-200, -300));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        testTexture = new Texture("assets/textures/test.jpg");

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

        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void update(float dt) {
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", TimeMgmt.getTime());

        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        testTexture.bind();
        glActiveTexture(GL_TEXTURE0);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        defaultShader.detach();
    }

    public void moveCamera(float dx, float dy) {
        camera.position.x -= dx;
        camera.position.y -= dy;
    }

}
