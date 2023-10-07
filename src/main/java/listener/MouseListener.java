package listener;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener INSTANCE;
    private double scrollX;
    private double scrollY;
    private double xPos;
    private double yPos;
    private double lastX;
    private double lastY;
    private boolean[] mouseBtnPressed = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0.0;
        yPos = 0.0;
        lastX = 0.0;
        lastY = 0.0;
    }

    public static MouseListener get() {
        if (MouseListener.INSTANCE == null) {
            MouseListener.INSTANCE = new MouseListener();
        }

        return MouseListener.INSTANCE;
    }

    public static void mousePositionCallback(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseBtnPressed[0] || get().mouseBtnPressed[1] || get().mouseBtnPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button < get().mouseBtnPressed.length) {
            if (action == GLFW_PRESS) {
                get().mouseBtnPressed[button] = true;
            } else if (action == GLFW_RELEASE) {
                get().mouseBtnPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void clear() {
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean isButtonPressed(int button) {
        if (button < get().mouseBtnPressed.length) {
            return get().mouseBtnPressed[button];
        }
        throw new IllegalStateException("Mouse button code larger than array");
    }


}
