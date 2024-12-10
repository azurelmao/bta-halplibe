package turniplabs.halplibe.helper;

import net.minecraft.client.render.BlockEntityRenderDispatcher;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.blockentity.TileEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.mixin.accessors.RenderManagerAccessor;
import turniplabs.halplibe.mixin.accessors.TileEntityRendererAccessor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class EntityHelper {

    @SuppressWarnings("unused")
    public static void createEntity(@NotNull Class<? extends Entity> clazz, @NotNull NamespaceID namespaceID, @Nullable String nameTranslationKey, @NotNull Supplier<EntityRenderer<?>> rendererSupplier) {
        EntityDispatcher.addMapping(clazz, namespaceID, nameTranslationKey);
        Assignment.queueEntityRenderer(clazz, rendererSupplier);
    }

    @SuppressWarnings({"unused", "deprecation"})
    public static void createEntity(@NotNull Class<? extends Entity> clazz, @NotNull NamespaceID namespaceID, @Nullable String nameTranslationKey, @NotNull Supplier<EntityRenderer<?>> rendererSupplier, @NotNull String legacyName, int legacyId) {
        EntityDispatcher.addMapping(clazz, namespaceID, nameTranslationKey);
        EntityDispatcher.Legacy.addMapping(clazz, legacyName, legacyId);
        Assignment.queueEntityRenderer(clazz, rendererSupplier);
    }

    @Deprecated
    public static void createEntity(@NotNull Class<? extends Entity> clazz, int id, String name, @NotNull Supplier<EntityRenderer<?>> rendererSupplier) {
        createEntity(clazz, new NamespaceID(HalpLibe.LEGACY_NAMESPACE, name), null, rendererSupplier, name, id);
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

    @Deprecated
    public static void createBlockEntity(Class<? extends TileEntity> clazz, String name) {
        createTileEntity(clazz, new NamespaceID(HalpLibe.LEGACY_NAMESPACE, name), name);
    }

    @SuppressWarnings("unused")
    public static void createSpecialTileEntity(@NotNull Class<? extends TileEntity> clazz, @NotNull NamespaceID name, @NotNull Supplier<TileEntityRenderer<?>> rendererSupplier) {
        TileEntityDispatcher.addMapping(clazz, name);
        Assignment.queueTileEntityRenderer(clazz, rendererSupplier);
    }

    @SuppressWarnings({"unused", "deprecation"})
    public static void createSpecialTileEntity(@NotNull Class<? extends TileEntity> clazz, @NotNull NamespaceID name, @NotNull Supplier<TileEntityRenderer<?>> rendererSupplier, @NotNull String legacyId) {
        TileEntityDispatcher.addMapping(clazz, name);
        TileEntityDispatcher.Legacy.addMapping(clazz, legacyId);
        Assignment.queueTileEntityRenderer(clazz, rendererSupplier);
    }

    @Deprecated
    public static void createSpecialBlockEntity(Class<? extends TileEntity> clazz, String name, Supplier<TileEntityRenderer<?>> rendererSupplier) {
        createSpecialTileEntity(clazz, new NamespaceID(HalpLibe.LEGACY_NAMESPACE, name), rendererSupplier, name);
    }

    public static class Assignment {
        public static boolean entityRendererDispatcherInitialized = false;
        public static final Map<Class<? extends Entity> , Supplier<EntityRenderer<?>>> queuedEntityRenderer = new LinkedHashMap<>();
        /**
         *  Queues aa EntityRenderer assignment until the game is ready to do so
         */
        public static void queueEntityRenderer(@NotNull Class<? extends Entity> clazz, @NotNull Supplier<EntityRenderer<?>> rendererSupplier){
            if (!HalpLibe.isClient) return;
            Objects.requireNonNull(rendererSupplier, "Renderer Supplier must not be null!");

            if (entityRendererDispatcherInitialized){
                Map<Class<? extends Entity>, EntityRenderer<?>> entityRenderMap = ((RenderManagerAccessor) EntityRenderDispatcher.instance).getEntityRenderMap();
                EntityRenderer<?> renderer = rendererSupplier.get();
                entityRenderMap.put(clazz, renderer);
                renderer.init(EntityRenderDispatcher.instance);
                return;
            }
            queuedEntityRenderer.put(clazz, rendererSupplier);
        }
        public static boolean TileEntityRendererDispatcherInitialized = false;
        public static final Map<Class<? extends TileEntity> , Supplier<TileEntityRenderer<?>>> queuedTileEntityRenderer = new LinkedHashMap<>();
        /**
         *  Queues a TileEntityRenderer assignment until the game is ready to do so
         */
        public static void queueTileEntityRenderer(@NotNull Class<? extends TileEntity> clazz, @NotNull Supplier<TileEntityRenderer<?>> rendererSupplier){
            if (!HalpLibe.isClient) return;
            Objects.requireNonNull(rendererSupplier, "Renderer Supplier must not be null!");

            if (TileEntityRendererDispatcherInitialized){
                Map<Class<? extends TileEntity>, TileEntityRenderer<?>> specialRendererMap = ((TileEntityRendererAccessor) BlockEntityRenderDispatcher.instance).getSpecialRendererMap();
                TileEntityRenderer<?> renderer = rendererSupplier.get();
                specialRendererMap.put(clazz, renderer);
                renderer.setRenderDispatcher(BlockEntityRenderDispatcher.instance);
                return;
            }
            queuedTileEntityRenderer.put(clazz, rendererSupplier);
        }
    }
}
