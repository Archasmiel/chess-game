package engine.utility;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private static KeyListener INSTANCE;
    private final boolean[] keyPressed = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (KeyListener.INSTANCE == null) {
            KeyListener.INSTANCE = new KeyListener();
        }
        return KeyListener.INSTANCE;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key < get().keyPressed.length) {
            if (action == GLFW_PRESS) {
                get().keyPressed[key] = true;
            } else if (action == GLFW_RELEASE) {
                get().keyPressed[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int key) {
        if (key < get().keyPressed.length) {
            return get().keyPressed[key];
        }
        throw new IllegalStateException("Key code is larger than array");
    }
}
