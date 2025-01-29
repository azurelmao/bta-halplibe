package turniplabs.halplibe.mixin.models;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.util.dispatch.Dispatcher;
import net.minecraft.core.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Mixin(value = BlockColorDispatcher.class, remap = false)
public abstract class BlockColorDispatcherMixin extends Dispatcher<Block<?>, BlockColor> {

    @Unique
    private final BlockColorDispatcher thisAs = (BlockColorDispatcher) (Object) this;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci) {
        ModelHelper.blockColorDispatcher = thisAs;
        FabricLoader.getInstance().getEntrypoints("initModels", ModelEntrypoint.class).forEach(e -> e.initBlockColors(thisAs));
    }
}
