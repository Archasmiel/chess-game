package engine.scene;

import static org.lwjgl.opengl.GL11.*;

public class LevelEditScene extends Scene {

    private LevelScene scene;

    public LevelEditScene(LevelScene scene) {
        this.scene = scene;
    }

    @Override
    public void update(float dt) {
        glClearColor(0f, 1f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
