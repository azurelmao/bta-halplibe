package turniplabs.halplibe.mixin.mixins.models;

import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemHelper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(TileEntityRenderDispatcher.class)
public class TileEntityRendererDispatcherMixin {
    @Shadow @Final private Map<Class<?>, TileEntityRenderer<?>> renderers;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        try {
            Field f = EntityHelper.class.getField("queuedTileEntityRenderer");
            f.setAccessible(true);
            Set<Map.Entry<Class<? extends TileEntity> , Supplier<TileEntityRenderer<?>>>> entries = ((Map<Class<? extends TileEntity> , Supplier<TileEntityRenderer<?>>>) f.get(null)).entrySet();
            f.setAccessible(false);
            for (Map.Entry<Class<? extends TileEntity> , Supplier<TileEntityRenderer<?>>> entry : entries){
                renderers.put(entry.getKey(), entry.getValue().get());
            }
            EntityHelper.class.getField("tileEntityRendererDispatcherInitialized").set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
