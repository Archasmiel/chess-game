package window;

import engine.utility.KeyListener;
import engine.utility.MouseListener;
import window.scene.LevelEditorScene;
import window.scene.LevelScene;
import window.scene.Scene;
import engine.lib.timer.ITimer;
import engine.lib.timer.SimpleTimer;
import engine.utility.GameTime;
import engine.lib.timer.Timers;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
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
            if (window.title == null || window.title.isEmpty()) {
                throw new IllegalStateException("Window title is empty");
            }
            return window;
        }

    }

    private int width;
    private int height;
    private String title;
    private Long glfwWindow;
    private static Scene currentScene;
    public static Window window = null;

    private Window() {

    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public void run() {
        System.out.printf("version: %s\n", Version.getVersion());
        initialize();
        loop();
        glfwFreeCallbacks(glfwWindow);
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public void initialize() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Not initialized GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
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
        glfwSwapInterval(0);
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop() {
        Timers timers = new Timers();
        timers.add(new SimpleTimer(0.5f));

        float beginTime = GameTime.getTime(), endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(1f, 1f, 1f, 1f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                changeScene(timers.get(0));
                moveCamera(dt);
                screenDraw(dt);
            }

            glfwSwapBuffers(glfwWindow);
            timers.tick(dt);

            endTime = GameTime.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    private void changeScene(ITimer timer) {
        if (timer.opened()) {
            if (KeyListener.isKeyPressed(GLFW_KEY_Q)) {
                Window.changeScene(0);
                timer.reset();
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
                Window.changeScene(1);
                timer.reset();
            }
        }
    }

    private void moveCamera(float dt) {
        if (currentScene instanceof LevelEditorScene) {
            LevelEditorScene scene = (LevelEditorScene) currentScene;
            if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
                scene.moveCamera(0, dt*100.0f);
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
                scene.moveCamera(0, -dt*100.0f);
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
                scene.moveCamera(-dt*100.0f, 0);
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
                scene.moveCamera(dt*100.0f, 0);
            }
        }
    }

    private void screenDraw(float dt) {
        currentScene.update(dt);
    }

}
