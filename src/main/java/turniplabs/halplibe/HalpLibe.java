package turniplabs.halplibe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLanternFirefly;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.RecipeEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.achievements.AchievementPage;
import turniplabs.halplibe.util.achievements.VanillaAchievementsPage;
import turniplabs.halplibe.util.toml.Toml;
import turniplabs.halplibe.util.version.PacketModList;

import java.util.HashMap;

public class HalpLibe implements ModInitializer, PreLaunchEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "halplibe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static boolean isClient = FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
    public static boolean sendModlist;
    public static boolean exportRecipes;
    public static boolean compatibilityMode;
    public static final TomlConfigHandler CONFIG;
    static {
        Toml toml = new Toml();
        toml.addCategory("Experimental");
        toml.addEntry("Experimental.AtlasWidth", "Dynamically resized the Terrain and Item atlases, value must be an integer greater than or equal to 32",32);
        toml.addEntry("Experimental.RequireTextures", "Require texture to exist on startup", false);
        toml.addEntry("Experimental.CompatibilityMode", "Attempt allowing compatibility with older halplibe versions", true);
        toml.addCategory("Network");
        toml.addEntry("Network.SendModlistPack", "This sends a modlist packet to clients that join the server when enabled, however it may cause issues if the clients do not have halplibe installed", true);
        toml.addCategory("Debug");
        toml.addEntry("Debug.ExportRecipes", "Writes all the loaded game recipes to dumpRecipes after startup", false);


        CONFIG = new TomlConfigHandler(MOD_ID, toml);

        Global.TEXTURE_ATLAS_WIDTH_TILES = Math.max(32, CONFIG.getInt("Experimental.AtlasWidth"));
        sendModlist = CONFIG.getBoolean("Network.SendModlistPack");
        exportRecipes = CONFIG.getBoolean("Debug.ExportRecipes");
        compatibilityMode = CONFIG.getBoolean("Experimental.CompatibilityMode");

        // Initialize Block and Item static fields
        try {
            Class.forName("net.minecraft.core.block.Block");
            Class.forName("net.minecraft.core.item.Item");
        } catch (ClassNotFoundException ignored) {
        }
        ModVersionHelper.initialize();
    }
    public static final AchievementPage VANILLA_ACHIEVEMENTS = new VanillaAchievementsPage();
    public static HashMap<String, Integer> itemKeyToIdMap = new HashMap<>();
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
    }

    @SuppressWarnings("unused")
    @Deprecated
    public static String addModId(String modId, String name) {
        return modId + "." + name;
    }

    @Override
    public void onInitialize() {
        AchievementHelper.addPage(VANILLA_ACHIEVEMENTS);
        LOGGER.info("HalpLibe initialized.");
    }

    public static GameRuleBoolean UNLOCK_ALL_RECIPES = GameRules.register(new GameRuleBoolean("unlockAllRecipes", false));

    @Override
    public void onPreLaunch() {
        // Initializes halp statics first
        NetworkHelper.register(PacketModList.class, false, true); // Register Halplibe packets first
    }

    @Override
    public void onRecipesReady() {

    }

    @Override
    public void initNamespaces() {

    }
}
