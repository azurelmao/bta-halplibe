package turniplabs.halplibe.mixin.models;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.BlockBuilder;

@Mixin(value = BlockModelDispatcher.class, remap = false)
public abstract class BlockModelDispatcherMixin {
    @Shadow public abstract void addDispatch(BlockModel<?> dispatchable);

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        for (BlockBuilder.Assignment.BlockAssignmentEntry<?> entry : BlockBuilder.Assignment.queuedBlockModels){
            try {
                addDispatch(entry.getModel());
            } catch (Exception e){
                throw new RuntimeException("Exception Occurred when applying " + entry.block.getKey(), e);
            }
        }
        BlockBuilder.Assignment.blockDispatcherInitialized = true;
    }
}
