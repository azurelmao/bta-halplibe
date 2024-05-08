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

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(value = TileEntityRenderDispatcher.class, remap = false)
public class TileEntityRendererDispatcherMixin {
    @Shadow @Final private Map<Class<?>, TileEntityRenderer<?>> renderers;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        Set<Map.Entry<Class<? extends TileEntity> , Supplier<TileEntityRenderer<?>>>> entries = EntityHelper.Assignment.queuedTileEntityRenderer.entrySet();
        for (Map.Entry<Class<? extends TileEntity> , Supplier<TileEntityRenderer<?>>> entry : entries){
            try {
                TileEntityRenderer<?> renderer = entry.getValue().get();
                renderers.put(entry.getKey(), renderer);
                renderer.setRenderDispatcher((TileEntityRenderDispatcher)(Object)this);
            } catch (Exception e){
                throw new RuntimeException("Exception Occurred when applying " + entry.getKey().getSimpleName(), e);
            }
        }
        EntityHelper.Assignment.tileEntityRendererDispatcherInitialized = true;
    }
}
