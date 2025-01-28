package turniplabs.halplibe.mixin.models;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.util.ModelEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

@Mixin(value = BlockModelDispatcher.class, remap = false)
public abstract class BlockModelDispatcherMixin {

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        FabricLoader.getInstance().getEntrypoints("initModels", ModelEntrypoint.class).forEach(ModelEntrypoint::initBlockModels);
    }
}
