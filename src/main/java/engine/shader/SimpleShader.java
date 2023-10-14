package engine.shader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class SimpleShader implements IShader {

    private final String filePath;
    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;


    public SimpleShader(String filePath) {
        this.filePath = filePath;
        try {
            String shaderCode = new String(Files.readAllBytes(Paths.get(filePath)));

            int vertex1 = shaderCode.indexOf("#type vertex");
            int fragment1 = shaderCode.indexOf("#type fragment");

            if (vertex1 == -1)
                throw new IOException("Error: not found start of vertex code");
            if (fragment1 == -1)
                throw new IOException("Error: not found start of fragment code");

            int vertex2 = shaderCode.indexOf("\r\n", vertex1 + 6);
            int fragment2 = shaderCode.indexOf("\r\n", fragment1 + 6);

            if (vertex1 < fragment1) {
                vertexSource = shaderCode.substring(vertex2, fragment1).trim();
                fragmentSource = shaderCode.substring(fragment2).trim();
            } else {
                vertexSource = shaderCode.substring(vertex2).trim();
                fragmentSource = shaderCode.substring(fragment2, vertex1).trim();
            }
        } catch (IOException e) {
            assert false : "Error: Couldn't open shader '" + filePath + "'";
            e.printStackTrace();
        }
    }

    /**
     * Compile and link shaders
     */
    @Override
    public void compile() {
        int vertexID, fragmentID;

        // vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\tVertex shader compile failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\tFragment shader compile failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // shader program
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\tLinking of shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    @Override
    public void use() {
        glUseProgram(shaderProgramID);
    }

    @Override
    public void detach() {
        glUseProgram(0);
    }
}
