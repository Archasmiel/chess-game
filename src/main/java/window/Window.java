package window;

import engine.listener.KeyListener;
import engine.listener.MouseListener;
import engine.scene.LevelEditScene;
import engine.scene.LevelScene;
import engine.scene.Scene;
import engine.time.timer.ITimer;
import engine.time.timer.SimpleTimer;
import engine.time.Time;
import engine.time.timer.Timers;
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
    private int currentScene = -1;
    private final List<Scene> allScenes = new ArrayList<>();

    private Window() {

    }

    public void addNewScene() {
        LevelScene scene = new LevelScene();
        allScenes.add(scene);
        LevelEditScene levelEditScene = new LevelEditScene(scene);
        allScenes.add(levelEditScene);
        scene.init();
        levelEditScene.init();
    }

    public void scrollScene() {
        if (currentScene == -1) {
            addNewScene();
            currentScene = 0;
            return;
        }
        if (currentScene+1 >= allScenes.size()) {
            currentScene = 0;
            return;
        }
        currentScene++;
    }

    public void descrollScene() {
        if (currentScene == -1) {
            addNewScene();
            currentScene = 0;
            return;
        }
        if (currentScene-1 <= 0) {
            currentScene = allScenes.size() - 1;
            return;
        }
        currentScene--;
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

        scrollScene();
    }

    public void loop() {
        float beginTime = Time.getTime(), endTime;
        float dt = -1.0f;
        Timers timers = new Timers();
        timers.add(new SimpleTimer(0.5f));
        timers.add(new SimpleTimer(0.5f));

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();



            if (dt >= 0) {
                ITimer timer = timers.get(0);
                if (timer.opened()) {
                    if (KeyListener.isKeyPressed(GLFW_KEY_Q)) {
                        scrollScene();
                        System.out.printf("scene %s/%s\n", currentScene, allScenes.size());
                        timer.reset();
                    }
                    if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
                        descrollScene();
                        System.out.printf("scene %s/%s\n", currentScene, allScenes.size());
                        timer.reset();
                    }
                }

                timer = timers.get(1);
                if (timer.opened()) {
                    if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
                        addNewScene();
                        System.out.printf("scene %s/%s\n", currentScene, allScenes.size());
                        timer.reset();
                    }
                }

                glClearColor(1f, 1f, 1f, 1f);
                glClear(GL_COLOR_BUFFER_BIT);
                allScenes.get(currentScene).update(dt);
            }

            glfwSwapBuffers(glfwWindow);
            timers.tick(dt);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

}
