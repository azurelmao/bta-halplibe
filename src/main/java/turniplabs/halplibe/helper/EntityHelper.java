package turniplabs.halplibe.helper;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.BlockEntityRenderDispatcher;
import net.minecraft.client.render.blockentity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.core.block.entity.BlockEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.mixin.accessors.RenderManagerAccessor;
import turniplabs.halplibe.mixin.accessors.BlockEntityAccessor;
import turniplabs.halplibe.mixin.accessors.BlockEntityRendererAccessor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class EntityHelper {
    public static void createEntity(Class<? extends Entity> clazz, int id, String name, @NotNull Supplier<EntityRenderer<?>> rendererSupplier) {
        EntityDispatcher.addMapping(clazz, name, id);
        Assignment.queueEntityRenderer(clazz, rendererSupplier);
    }

    public static void createBlockEntity(Class<? extends BlockEntity> clazz, String name) {
        BlockEntityAccessor.callAddMapping(clazz, name);
    }

    public static void createSpecialBlockEntity(Class<? extends BlockEntity> clazz, String name, Supplier<BlockEntityRenderer<?>> rendererSupplier) {
        BlockEntityAccessor.callAddMapping(clazz, name);
        Assignment.queueBlockEntityRenderer(clazz, rendererSupplier);
    }
    public static class Assignment {
        public static boolean entityRendererDispatcherInitialized = false;
        public static final Map<Class<? extends Entity> , Supplier<EntityRenderer<?>>> queuedEntityRenderer = new LinkedHashMap<>();
        /**
         *  Queues aa EntityRenderer assignment until the game is ready to do so
         */
        public static void queueEntityRenderer(@NotNull Class<? extends Entity> clazz, @NotNull Supplier<EntityRenderer<?>> rendererSupplier){
            if (!HalpLibe.isClient) return;
            if (rendererSupplier == null) return;

            if (entityRendererDispatcherInitialized){
                Map<Class<? extends Entity>, EntityRenderer<?>> entityRenderMap = ((RenderManagerAccessor) EntityRenderDispatcher.instance).getEntityRenderMap();
                EntityRenderer<?> renderer = rendererSupplier.get();
                entityRenderMap.put(clazz, renderer);
                renderer.init(EntityRenderDispatcher.instance);
                return;
            }
            queuedEntityRenderer.put(clazz, rendererSupplier);
        }
        public static boolean BlockEntityRendererDispatcherInitialized = false;
        public static final Map<Class<? extends BlockEntity> , Supplier<BlockEntityRenderer<?>>> queuedBlockEntityRenderer = new LinkedHashMap<>();
        /**
         *  Queues a BlockEntityRenderer assignment until the game is ready to do so
         */
        public static void queueBlockEntityRenderer(@NotNull Class<? extends BlockEntity> clazz, @NotNull Supplier<BlockEntityRenderer<?>> rendererSupplier){
            if (!HalpLibe.isClient) return;
            if (rendererSupplier == null) return;

            if (BlockEntityRendererDispatcherInitialized){
                Map<Class<? extends BlockEntity>, BlockEntityRenderer<?>> specialRendererMap = ((BlockEntityRendererAccessor) BlockEntityRenderDispatcher.instance).getSpecialRendererMap();
                BlockEntityRenderer<?> renderer = rendererSupplier.get();
                specialRendererMap.put(clazz, renderer);
                renderer.setRenderDispatcher(BlockEntityRenderDispatcher.instance);
                return;
            }
            queuedBlockEntityRenderer.put(clazz, rendererSupplier);
        }
    }
}
