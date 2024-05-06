package turniplabs.halplibe.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectoryManager {
    public static final HashMap<String, HashMap<String, String>> resourceDirectoryMap = new HashMap<>();
    public static final List<String> allKeys = new ArrayList<>();
    public static final String BLOCK_TEXTURES = registerKey("halplibe:block_textures");
    public static final String ITEM_TEXTURES = registerKey("halplibe:item_textures");
    public static final String PARTICLE_TEXTURES = registerKey("halplibe:particle_textures");
    public static final String LANGUAGE = registerKey("halplibe:language");
    public static final String SOUND = registerKey("halplibe:sound");
    public static final String MUSIC = registerKey("halplibe:music");
    public static final String STREAMING = registerKey("halplibe:streaming");
    public static final String CAVE_MUSIC = registerKey("halplibe:cave_music");
    public static final String ARMOR = registerKey("halplibe:armor");
    public static final FabricLoader fl = FabricLoader.getInstance();
    static {
        refreshDirectories();
    }
    public static void refreshDirectories(){
        for (ModContainer container : fl.getAllMods()){
            String id = container.getMetadata().getId();
            ModMetadata metadata = container.getMetadata();
            for (String key : allKeys){
                if (metadata.containsCustomValue(key)){
                    assignDirectory(key, id, metadata.getCustomValue(key).getAsString());
                }
            }
        }
    }
    public static String registerKey(String key){
        allKeys.add(key);
        return key;
    }
    public static void assignDirectory(String key, String modID, String path){
        resourceDirectoryMap.putIfAbsent(modID, new HashMap<>());
        resourceDirectoryMap.get(modID).put(key, path);
    }
    public static String getDirectory(String key, String modID, String formattedDefault){
        resourceDirectoryMap.putIfAbsent(modID, new HashMap<>());
        return resourceDirectoryMap.get(modID).getOrDefault(key, String.format(formattedDefault, modID));
    }
    public static String getBlockTextureDirectory(String modID){
        return getDirectory(BLOCK_TEXTURES, modID, "/assets/%s/textures/block/");
    }
    public static String getItemTextureDirectory(String modID){
        return getDirectory(ITEM_TEXTURES, modID, "/assets/%s/textures/item/");
    }
    public static String getParticleTextureDirectory(String modID){
        return getDirectory(PARTICLE_TEXTURES, modID, "/assets/%s/textures/particle/");
    }
    public static String getLanguageDirectory(String modID){
        return getDirectory(LANGUAGE, modID, "/lang/%s/");
    }
    public static String getSoundDirectory(String modID){
        return getDirectory(SOUND, modID, "/assets/%s/sound/");
    }
    public static String getMusicDirectory(String modID){
        return getDirectory(MUSIC, modID, "/assets/%s/music/");
    }
    public static String getCaveMusicDirectory(String modID){
        return getDirectory(CAVE_MUSIC, modID, "/assets/%s/cavemusic/");
    }
    public static String getStreamingDirectory(String modID){
        return getDirectory(STREAMING, modID, "/assets/%s/streaming/");
    }
    public static String getArmorDirectory(String modID){
        return getDirectory(ARMOR, modID, "/assets/%s/textures/armor/");
    }
}
