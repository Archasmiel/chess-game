package window;

import listener.KeyListener;
import listener.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    public static class Builder {

        private final Window window;

        public Builder() {
            window = new Window();
        }

        public Builder size(int width, int height) {
            window.width = width;
            window.height = height;
            return this;
        }

        public Builder title(String title) {
            window.title = title;
            return this;
        }

        public Window build() {
            if (window.width <= 0 || window.height <= 0) {
                throw new IllegalStateException("Window size(-s) are incorrect");
            }
            if (window.title == null || window.title.equals("")) {
                throw new IllegalStateException("Window title is empty");
            }
            return window;
        }

    }

    private int width;
    private int height;
    private String title;
    private Long glfwWindow;

    private Window() {

    }

    public void run() {
        System.out.printf("version: %s\n", Version.getVersion());
        initialize();
        loop();
        glfwFreeCallbacks(glfwWindow);
    }

    public void initialize() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Not initialized GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Window not created");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    public void loop() {
        float r = 0, g = 0, b = 0, a = 0;
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (KeyListener.isKeyPressed(GLFW_KEY_Q)) {
                r += 0.01f;
                r %= 1f;
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
                g += 0.01f;
                g %= 1f;
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
                b += 0.01f;
                b %= 1f;
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_R)) {
                a += 0.01f;
                a %= 1f;
            }

            glfwSwapBuffers(glfwWindow);
        }
    }

}
