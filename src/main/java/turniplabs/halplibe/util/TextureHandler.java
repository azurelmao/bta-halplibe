package turniplabs.halplibe.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.client.util.helper.Textures;

import java.awt.image.BufferedImage;

import static turniplabs.halplibe.HalpLibe.CONFIG;
import static turniplabs.halplibe.HalpLibe.LOGGER;

public class TextureHandler extends DynamicTexture {

    private final String textureName;
    private int frameCount;
    private final String animationSource;
    private final int textureIndex;
    private int resolution;
    private final int defaultResolution;
    private final int width;
    private byte[] frames;
    private int elapsedTicks = 0;
    private float scale;
    private boolean hasInitialized = false;

    public TextureHandler(String textureName, String animationSource, int textureIndex, int resolution, int width) {
        this(textureName, animationSource, textureIndex, resolution, width, null);
    }
    public TextureHandler(String textureName, String animationSource, int textureIndex, int defaultResolution, int width, Minecraft mc) {
        super(textureIndex, getAtlasResolution(textureName, defaultResolution), width);
        this.textureName = textureName;
        this.animationSource = animationSource;
        this.textureIndex = textureIndex;
        this.defaultResolution = defaultResolution;
        this.width = width;
        if (mc == null){return;} // Don't process textures when mc is null
        BufferedImage image = Textures.readImage(mc.texturePackList.selectedTexturePack.getResourceAsStream(animationSource));
        if (image == null) {return;} // Don't process non existent images

        if (image == Textures.missingTexture) {
            if (CONFIG.getBoolean("Experimental.RequireTextures")){
                throw new RuntimeException("Animation " + animationSource + " couldn't be found!");
            }
            LOGGER.warn("Animation " + animationSource + " couldn't be found!");
            image = Textures.readImage(Textures.class.getResourceAsStream("/assets/halplibe/block/missing.png"));
        }
        if (image.getHeight() % image.getWidth() != 0) {
            throw new RuntimeException("Invalid Height for animation! " + animationSource);
        }
        this.resolution = image.getWidth();
        // Scaling factor from source resolution to destination resolution
        this.scale = getScale(textureName, resolution);
        this.frameCount = image.getHeight() / image.getWidth();
        if (frameCount < 1){
            frameCount = 1;
        }
        System.out.println(animationSource + " frame Count: " + this.frameCount);
        this.frames = new byte[(int) (resolution * resolution * 4 * this.frameCount * scale * scale)];

        for (int frame = 0; frame < this.frameCount; ++frame) {
            for (int x = 0; x < resolution * scale; ++x) {
                for (int y = 0; y < resolution * scale; ++y) {
                    int c = image.getRGB((int) (x/scale),  (frame * resolution + (int)(y/scale)));
                    putPixel(this.frames, (int) (frame * resolution * scale * scale * resolution + y * resolution * scale + x), c);
                }
            }
        }
        hasInitialized = true;
    }
    public TextureHandler newHandler(Minecraft mc){
        // Returns a new TextureHandler using the current state of Minecraft, If the texturepack has changed it will then use the new texturepack's textures
        return new TextureHandler(textureName, animationSource, textureIndex, defaultResolution, width, mc);
    }
    public void update() {
        if (!hasInitialized) {return;}

        this.elapsedTicks = (this.elapsedTicks + 1) % this.frameCount;

        for (int i = 0; i < this.resolution * scale; ++i) {
            for (int j = 0; j < this.resolution * scale; ++j) {
                transferPixel(this.frames, (int) (this.elapsedTicks * this.resolution * this.resolution * scale * scale + j * this.resolution * scale + i), this.imageData, (int) (j * this.resolution * scale + i));
            }
        }
    }
    public String getTextureName() {
        return this.textureName;
    }
    public static float getScale(String textureName, int resolution){
        if (TextureHelper.textureDestinationResolutions.get(textureName) != null){
            return (float)TextureHelper.textureDestinationResolutions.get(textureName)/resolution;
        }
        return 1f;
    }
    public static int getAtlasResolution(String textureName, int defaultResolution){
        if (TextureHelper.textureDestinationResolutions.get(textureName) != null){
            return TextureHelper.textureDestinationResolutions.get(textureName);
        }
        return defaultResolution;
    }

    // Copied the old method from 7.0 since it was deleted in 7.1-pre1
    public static void transferPixel(byte[] array1, int i, byte[] array2, int j) {
        array2[j * 4] = array1[i * 4];
        array2[j * 4 + 1] = array1[i * 4 + 1];
        array2[j * 4 + 2] = array1[i * 4 + 2];
        array2[j * 4 + 3] = array1[i * 4 + 3];
    }
}
