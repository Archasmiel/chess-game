package window.scene;

import engine.graphics.camera.Camera;
import engine.graphics.render.Transform;
import engine.objects.GameObject;
import engine.objects.components.SpriteRenderer;
import engine.objects.components.Spritesheet;
import engine.utility.AssetPool;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(0, 0));

        Spritesheet ss1 = AssetPool.getSpritesheet("assets/textures/spritesheet.png");

        GameObject go1 = new GameObject("Object 1", new Transform(
                new Vector2f(100, 100), new Vector2f(256, 256)
        ));
        go1.addComponent(new SpriteRenderer(ss1.getSprite(0)));
        this.addGameObjectToScene(go1);

        GameObject go2 = new GameObject("Object 2", new Transform(
                new Vector2f(400, 100), new Vector2f(256, 256)
        ));
        go2.addComponent(new SpriteRenderer(ss1.getSprite(1)));
        this.addGameObjectToScene(go2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/textures/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/textures/spritesheet.png"),
                        16, 16, 26, 0));
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
