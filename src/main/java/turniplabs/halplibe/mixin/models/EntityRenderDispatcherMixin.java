package turniplabs.halplibe.mixin.models;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.core.entity.Entity;
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

@Mixin(value = EntityRenderDispatcher.class, remap = false)
public abstract class EntityRenderDispatcherMixin {
    @Shadow @Final private Map<Class<?>, EntityRenderer<?>> renderers;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        Set<Map.Entry<Class<? extends Entity> , Supplier<EntityRenderer<?>>>> entries = EntityHelper.Assignment.queuedEntityRenderer.entrySet();
        for (Map.Entry<Class<? extends Entity> , Supplier<EntityRenderer<?>>> entry : entries){
            try {
                EntityRenderer<?> renderer = entry.getValue().get();
                renderers.put(entry.getKey(), renderer);
                renderer.init((EntityRenderDispatcher) (Object)this);
            } catch (Exception e){
                throw new RuntimeException("Exception Occurred when applying " + entry.getKey().getSimpleName(), e);
            }
        }
        EntityHelper.Assignment.entityRendererDispatcherInitialized = true;
    }
}
