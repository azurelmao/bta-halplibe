package turniplabs.halplibe.helper;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import turniplabs.halplibe.mixin.accessors.RenderManagerAccessor;
import turniplabs.halplibe.mixin.accessors.TileEntityAccessor;
import turniplabs.halplibe.mixin.accessors.TileEntityRendererAccessor;

import java.util.Map;

abstract public class EntityHelper {
    /**
     * @deprecated No longer works serverside as of BTA 7.1 due to the removal of "Minecraft.class" from the server jar
     * Replaced by {@link Core#createEntity(Class, int, String)} and {@link Client#assignEntityRenderer(Class, EntityRenderer)}
     */
    @Deprecated
    public static void createEntity(Class<? extends Entity> clazz, EntityRenderer<?> renderer, int id, String name) {
        Client.assignEntityRenderer(clazz, renderer);
        Core.createEntity(clazz, id, name);
    }

    /**
     * @deprecated Function is being moved to {@link Core#createTileEntity(Class, String)}
     */
    @Deprecated
    public static void createTileEntity(Class<? extends TileEntity> clazz, String name) {
        Core.createTileEntity(clazz, name);
    }

    /**
     * @deprecated Function is being moved to {@link Core#createSpecialTileEntity(Class, TileEntityRenderer, String)}
     */
    @Deprecated
    public static void createSpecialTileEntity(Class<? extends TileEntity> clazz, TileEntityRenderer<?> renderer, String name) {
        Core.createSpecialTileEntity(clazz, renderer, name);
    }
    /**
     * Functions to call from the client or server
     */
    public static class Core {
        public static void createEntity(Class<? extends Entity> clazz, int id, String name) {
            EntityDispatcher.addMapping(clazz, name, id);
        }
        public static void createTileEntity(Class<? extends TileEntity> clazz, String name) {
            TileEntityAccessor.callAddMapping(clazz, name);
        }

        /**
         * @deprecated Function is being split into {@link Core#createTileEntity(Class, String)} and {@link Client#assignTileEntityRenderer(Class, TileEntityRenderer)}
         */
        @Deprecated
        public static void createSpecialTileEntity(Class<? extends TileEntity> clazz, TileEntityRenderer<?> renderer, String name) {
            Map<Class<? extends TileEntity>, TileEntityRenderer<?>> specialRendererMap = ((TileEntityRendererAccessor) TileEntityRenderDispatcher.instance).getSpecialRendererMap();
            specialRendererMap.put(clazz, renderer);
            renderer.setRenderDispatcher(TileEntityRenderDispatcher.instance);

            TileEntityAccessor.callAddMapping(clazz, name);
        }
    }

    /**
     * Functions to only call from the client
     */
    public static class Client {
        public static void assignEntityRenderer(Class<? extends Entity> clazz, EntityRenderer<?> renderer){
            Map<Class<? extends Entity>, EntityRenderer<?>> entityRenderMap = ((RenderManagerAccessor) EntityRenderDispatcher.instance).getEntityRenderMap();
            entityRenderMap.put(clazz, renderer);
            renderer.setRenderDispatcher(EntityRenderDispatcher.instance);
        }
        @SuppressWarnings("unused") // API function
        public static void assignTileEntityRenderer(Class<? extends TileEntity> clazz, TileEntityRenderer<?> renderer){
            Map<Class<? extends TileEntity>, TileEntityRenderer<?>> specialRendererMap = ((TileEntityRendererAccessor) TileEntityRenderDispatcher.instance).getSpecialRendererMap();
            specialRendererMap.put(clazz, renderer);
            renderer.setRenderDispatcher(TileEntityRenderDispatcher.instance);
        }
    }
}
