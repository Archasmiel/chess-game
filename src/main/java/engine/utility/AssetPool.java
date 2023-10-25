package engine.utility;

import engine.graphics.render.Shader;
import engine.graphics.render.Texture;
import engine.objects.components.Spritesheet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static final Map<String, Shader> SHADERS = new HashMap<>();
    private static final Map<String, Texture> TEXTURES = new HashMap<>();
    private static final Map<String, Spritesheet> SPRITESHEETS = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if (SHADERS.containsKey(file.getAbsolutePath())) {
            return SHADERS.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            SHADERS.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (TEXTURES.containsKey(file.getAbsolutePath())) {
            return TEXTURES.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture(resourceName);
            TEXTURES.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        File file = new File(resourceName);
        if (!AssetPool.SPRITESHEETS.containsKey(file.getAbsolutePath())) {
            AssetPool.SPRITESHEETS.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        assert AssetPool.SPRITESHEETS.containsKey(file.getAbsolutePath()) : "Error: Tried to access spritesheet '" + resourceName + "' and it is not in pool";
        return AssetPool.SPRITESHEETS.getOrDefault(file.getAbsolutePath(), null);
    }

}
