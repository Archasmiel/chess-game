package window;

import engine.utility.KeyListener;
import engine.utility.MouseListener;
import engine.graphics.scene.LevelEditScene;
import engine.graphics.scene.LevelScene;
import engine.graphics.scene.Scene;
import engine.lib.timer.ITimer;
import engine.lib.timer.SimpleTimer;
import engine.utility.TimeMgmt;
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
    private int currentScene = -1;
    private final List<Scene> allScenes = new ArrayList<>();

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

        LevelScene scene = new LevelScene();
        LevelEditScene levelEditScene = new LevelEditScene(scene);
        allScenes.add(levelEditScene);
        allScenes.add(scene);
        currentScene = 0;
        levelEditScene.init();
        scene.init();
    }

    public void loop() {
        Timers timers = new Timers();
        timers.add(new SimpleTimer(0.5f));

        float beginTime = TimeMgmt.getTime(), endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            if (dt >= 0) {

                // scene switching
                ITimer timer = timers.get(0);
                if (timer.opened()) {
                    if (KeyListener.isKeyPressed(GLFW_KEY_Q)) {
                        currentScene = currentScene == 0 ? 0 : 1;
                        allScenes.get(currentScene).init();
                        System.out.printf("scene %s/%s\n", currentScene, allScenes.size());
                        timer.reset();
                    }
                    if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
                        currentScene = currentScene == 0 ? 1 : 0;
                        allScenes.get(currentScene).init();
                        System.out.printf("scene %s/%s\n", currentScene, allScenes.size());
                        timer.reset();
                    }
                }

                // camera moving
                if (allScenes.get(currentScene) instanceof LevelEditScene) {
                    LevelEditScene scene = (LevelEditScene) allScenes.get(currentScene);
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

                glClearColor(1f, 1f, 1f, 1f);
                glClear(GL_COLOR_BUFFER_BIT);
                allScenes.get(currentScene).update(dt);
            }

            glfwSwapBuffers(glfwWindow);
            timers.tick(dt);

            endTime = TimeMgmt.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

}
