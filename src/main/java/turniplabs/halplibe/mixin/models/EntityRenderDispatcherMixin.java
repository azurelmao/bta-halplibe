package turniplabs.halplibe.mixin.models;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.core.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(value = EntityRenderDispatcher.class, remap = false)
public abstract class EntityRenderDispatcherMixin {
    @Shadow @Final private Map<Class<?>, EntityRenderer<?>> renderers;

    @Unique
    private final EntityRenderDispatcher thisAs = (EntityRenderDispatcher) (Object) this;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        ModelHelper.entityRenderers = renderers;
        ModelHelper.entityRenderDispatcher = thisAs;
        FabricLoader.getInstance().getEntrypoints("initModels", ModelEntrypoint.class).forEach(e -> e.initEntityModels(thisAs));
    }
}
