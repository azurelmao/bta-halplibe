package turniplabs.halplibe.mixin.mixins.models;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.BlockBuilder;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Mixin(value = BlockModelDispatcher.class, remap = false)
public abstract class BlockModelDispatcherMixin {
    @Shadow public abstract void addDispatch(BlockModel dispatchable);

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        Set<Map.Entry<Block, Function<Block, BlockModel<?>>>>entries = BlockBuilder.Assignment.queuedBlockModels.entrySet();
        for (Map.Entry<Block, Function<Block, BlockModel<?>>> entry : entries){
            try {
                addDispatch(entry.getValue().apply(entry.getKey()));
            } catch (Exception e){
                throw new RuntimeException("Exception Occurred when applying " + entry.getKey().getKey(), e);
            }
        }
        BlockBuilder.Assignment.blockDispatcherInitialized = true;
    }
}
