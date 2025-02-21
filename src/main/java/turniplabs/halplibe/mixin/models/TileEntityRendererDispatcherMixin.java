package turniplabs.halplibe.mixin.models;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import java.util.Map;

@Mixin(value = TileEntityRenderDispatcher.class, remap = false)
public class TileEntityRendererDispatcherMixin {
    @Shadow @Final private Map<Class<?>, TileEntityRenderer<?>> renderers;

    @Unique
    private final TileEntityRenderDispatcher thisAs = (TileEntityRenderDispatcher) (Object) this;

    @Inject(method = "<init>()V", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;", shift = At.Shift.BEFORE))
    private void addQueuedModels(CallbackInfo ci){
        ModelHelper.tileEntityRenderers = renderers;
        ModelHelper.tileEntityRenderDispatcher = thisAs;
        FabricLoader.getInstance().getEntrypoints("initModels", ModelEntrypoint.class).forEach(e -> e.initTileEntityModels(thisAs));
    }
}
