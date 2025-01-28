package turniplabs.halplibe.helper;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EntityHelper {

    @SuppressWarnings("unused")
    public static void createEntity(@NotNull Class<? extends Entity> clazz, @NotNull NamespaceID namespaceID, @Nullable String nameTranslationKey) {
        EntityDispatcher.addMapping(clazz, namespaceID, nameTranslationKey);
    }

    @SuppressWarnings({"unused", "deprecation"})
    public static void createEntity(@NotNull Class<? extends Entity> clazz, @NotNull NamespaceID namespaceID, @Nullable String nameTranslationKey, @NotNull String legacyName, int legacyId) {
        EntityDispatcher.addMapping(clazz, namespaceID, nameTranslationKey);
        EntityDispatcher.Legacy.addMapping(clazz, legacyName, legacyId);
    }

    @SuppressWarnings("unused")
    public static void createTileEntity(@NotNull Class<? extends TileEntity> clazz, @NotNull NamespaceID name) {
        TileEntityDispatcher.addMapping(clazz, name);
    }

    @SuppressWarnings({"unused", "deprecation"})
    public static void createTileEntity(@NotNull Class<? extends TileEntity> clazz, @NotNull NamespaceID name, @NotNull String legacyId) {
        TileEntityDispatcher.addMapping(clazz, name);
        TileEntityDispatcher.Legacy.addMapping(clazz, legacyId);
    }
}
