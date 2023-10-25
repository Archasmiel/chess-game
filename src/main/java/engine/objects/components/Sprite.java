package engine.objects.components;

import engine.graphics.render.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Sprite {

    private final Vector2f[] texCoords;
    private final Texture texture;

    public Sprite(Texture texture) {
        this.texture = texture;
        texCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
    }

    public Sprite(Texture texture, Vector2f[] texCoords) {
        this.texCoords = texCoords;
        this.texture = texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public Texture getTexture() {
        return texture;
    }
}
