package turniplabs.halplibe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.util.HashMap;

public class HalpLibe implements ModInitializer, PreLaunchEntrypoint{
    public static final String MOD_ID = "halplibe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final boolean isClient = FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
    public static final TomlConfigHandler CONFIG;
    static {
        Toml toml = new Toml();
        CONFIG = new TomlConfigHandler(MOD_ID, toml);
    }

    /*public static HashMap<String, Integer> itemKeyToIdMap = new HashMap<>();
    public static int getTrueItemOrBlockId(String key){
        // This all exists since the item key to id maps are somewhat unreliable due to blocks having their keys remapped after creation
        if (itemKeyToIdMap.containsKey(key)) return itemKeyToIdMap.get(key);
        if (key.startsWith("item")){
            for (Item item : Item.itemsList){
                if (item != null && item.getKey() != null && !item.getKey().isEmpty()){
                    itemKeyToIdMap.put(item.getKey(), item.id);
                    if (item.getKey().matches(key)) return item.id;
                }
            }
            throw new IllegalArgumentException("Could not find an item that corresponds to the key '" + key + "'");
        }
        if (key.startsWith("tile")){
            for (Block item : Block.blocksList){
                if (item != null && item.getKey() != null && !item.getKey().isEmpty()){
                    itemKeyToIdMap.put(item.getKey(), item.id);
                    if (item.getKey().matches(key)) return item.id;
                }
            }
            throw new IllegalArgumentException("Could not find a block that corresponds to the key '" + key + "'");
        }
        throw new IllegalArgumentException("Key '" + key + "' does not start with a valid predicate of 'item' or 'tile'");
    }*/

    @SuppressWarnings("unused")
    @Deprecated
    public static String addModId(String modId, String name) {
        return modId + "." + name;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("HalpLibe initialized.");
    }

    @Override
    public void onPreLaunch() {

    }

}
