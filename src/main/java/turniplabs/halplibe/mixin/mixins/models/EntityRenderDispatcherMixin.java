package turniplabs.halplibe.mixin.mixins.models;

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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(value = EntityRenderDispatcher.class, remap = false)
public abstract class EntityRenderDispatcherMixin {
    @Shadow @Final private Map<Class<?>, EntityRenderer<?>> renderers;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        try {
            Field f = EntityHelper.class.getField("queuedEntityRenderer");
            f.setAccessible(true);
            Set<Map.Entry<Class<? extends Entity> , Supplier<EntityRenderer<?>>>> entries = ((Map<Class<? extends Entity> , Supplier<EntityRenderer<?>>>) f.get(null)).entrySet();
            f.setAccessible(false);
            for (Map.Entry<Class<? extends Entity> , Supplier<EntityRenderer<?>>> entry : entries){
                renderers.put(entry.getKey(), entry.getValue().get());
            }
            EntityHelper.class.getField("entityRendererDispatcherInitialized").set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
