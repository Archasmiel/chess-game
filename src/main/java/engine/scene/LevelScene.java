package engine.scene;

import static org.lwjgl.opengl.GL11.*;

public class LevelScene extends Scene{
    public LevelScene() {
    }

    @Override
    public void update(float dt) {
        glClearColor(1f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
