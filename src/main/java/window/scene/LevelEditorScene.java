package window.scene;

import engine.asset.AssetPool;
import engine.graphics.camera.Camera;
import engine.objects.GameObject;
import engine.graphics.render.Transform;
import engine.objects.components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

        GameObject go1 = new GameObject("Object 1", new Transform(
                new Vector2f(100, 100), new Vector2f(256, 256)
        ));
        go1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/textures/test.jpg")));
        this.addGameObjectToScene(go1);

        GameObject go2 = new GameObject("Object 2", new Transform(
                new Vector2f(400, 100), new Vector2f(256, 256)
        ));
        go2.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/textures/testImage.jpg")));
        this.addGameObjectToScene(go2);

        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(float dt) {
        // System.out.println("FPS: " + (1.0f / dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    public void moveCamera(float dx, float dy) {
        this.camera.position.x += dx;
        this.camera.position.y += dy;
    }

}
