package turniplabs.halplibe.helper;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.mixin.accessors.RenderManagerAccessor;
import turniplabs.halplibe.mixin.accessors.TileEntityAccessor;
import turniplabs.halplibe.mixin.accessors.TileEntityRendererAccessor;
import turniplabs.halplibe.mixin.mixins.models.EntityRenderDispatcherMixin;
import turniplabs.halplibe.mixin.mixins.models.TileEntityRendererDispatcherMixin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

final public class EntityHelper {
    /**
     * Used in {@link EntityRenderDispatcherMixin#addQueuedModels(CallbackInfo)}
     */
    private static boolean entityRendererDispatcherInitialized = false;
    private static final Map<Class<? extends Entity> , Supplier<EntityRenderer<?>>> queuedEntityRenderer = new HashMap<>();
    public static void queueEntityRenderer(@NotNull Class<? extends Entity> clazz, @NotNull Supplier<EntityRenderer<?>> rendererSupplier){
        if (!HalpLibe.isClient) return;
        if (rendererSupplier == null) return;

        if (entityRendererDispatcherInitialized){
            Map<Class<? extends Entity>, EntityRenderer<?>> entityRenderMap = ((RenderManagerAccessor) EntityRenderDispatcher.instance).getEntityRenderMap();
            EntityRenderer<?> renderer = rendererSupplier.get();
            entityRenderMap.put(clazz, renderer);
            renderer.setRenderDispatcher(EntityRenderDispatcher.instance);
            return;
        }
        queuedEntityRenderer.put(clazz, rendererSupplier);
    }
    /**
     * Used in {@link TileEntityRendererDispatcherMixin#addQueuedModels(CallbackInfo)}
     */
    private static boolean tileEntityRendererDispatcherInitialized = false;
    private static final Map<Class<? extends TileEntity> , Supplier<TileEntityRenderer<?>>> queuedTileEntityRenderer = new HashMap<>();
    public static void queueTileEntityRenderer(@NotNull Class<? extends TileEntity> clazz, @NotNull Supplier<TileEntityRenderer<?>> rendererSupplier){
        if (!HalpLibe.isClient) return;
        if (rendererSupplier == null) return;

        if (tileEntityRendererDispatcherInitialized){
            Map<Class<? extends TileEntity>, TileEntityRenderer<?>> specialRendererMap = ((TileEntityRendererAccessor) TileEntityRenderDispatcher.instance).getSpecialRendererMap();
            TileEntityRenderer<?> renderer = rendererSupplier.get();
            specialRendererMap.put(clazz, renderer);
            renderer.setRenderDispatcher(TileEntityRenderDispatcher.instance);
            return;
        }
        queuedTileEntityRenderer.put(clazz, rendererSupplier);
    }
    public static void createEntity(Class<? extends Entity> clazz, int id, String name, @NotNull Supplier<EntityRenderer<?>> rendererSupplier) {
        EntityDispatcher.addMapping(clazz, name, id);
        queueEntityRenderer(clazz, rendererSupplier);
    }

    public static void createTileEntity(Class<? extends TileEntity> clazz, String name) {
        TileEntityAccessor.callAddMapping(clazz, name);
    }

    public static void createSpecialTileEntity(Class<? extends TileEntity> clazz, String name, Supplier<TileEntityRenderer<?>> rendererSupplier) {
        TileEntityAccessor.callAddMapping(clazz, name);
        queueTileEntityRenderer(clazz, rendererSupplier);
    }
}
