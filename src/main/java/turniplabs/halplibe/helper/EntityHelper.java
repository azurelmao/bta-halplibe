package turniplabs.halplibe.helper;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.mixin.accessors.RenderManagerAccessor;
import turniplabs.halplibe.mixin.accessors.TileEntityAccessor;
import turniplabs.halplibe.mixin.accessors.TileEntityRendererAccessor;

import java.util.Map;
import java.util.function.Supplier;

abstract public class EntityHelper {

    public static void createEntity(Class<? extends Entity> clazz, int id, String name, Supplier<EntityRenderer<?>> rendererSupplier) {
        EntityDispatcher.addMapping(clazz, name, id);
        if (HalpLibe.isClient){
            Map<Class<? extends Entity>, EntityRenderer<?>> entityRenderMap = ((RenderManagerAccessor) EntityRenderDispatcher.instance).getEntityRenderMap();
            EntityRenderer<?> renderer = rendererSupplier.get();
            entityRenderMap.put(clazz, renderer);
            renderer.setRenderDispatcher(EntityRenderDispatcher.instance);
        }
    }

    public static void createTileEntity(Class<? extends TileEntity> clazz, String name) {
        TileEntityAccessor.callAddMapping(clazz, name);
    }

    public static void createSpecialTileEntity(Class<? extends TileEntity> clazz, String name, Supplier<TileEntityRenderer<?>> rendererSupplier) {
        TileEntityAccessor.callAddMapping(clazz, name);
        if (HalpLibe.isClient){
            Map<Class<? extends TileEntity>, TileEntityRenderer<?>> specialRendererMap = ((TileEntityRendererAccessor) TileEntityRenderDispatcher.instance).getSpecialRendererMap();
            TileEntityRenderer<?> renderer = rendererSupplier.get();
            specialRendererMap.put(clazz, renderer);
            renderer.setRenderDispatcher(TileEntityRenderDispatcher.instance);
        }
    }
}
