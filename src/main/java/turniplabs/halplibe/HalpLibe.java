package turniplabs.halplibe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.NetworkHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.ModelEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.util.HashMap;

public class HalpLibe implements ModInitializer, PreLaunchEntrypoint {
    public static final String MOD_ID = "halplibe";
    public static final String LEGACY_NAMESPACE = "halplibe_legacy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final boolean isClient = FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
    public static final TomlConfigHandler CONFIG;
    static {
        Toml toml = new Toml();
        CONFIG = new TomlConfigHandler(MOD_ID, toml);
    }

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
