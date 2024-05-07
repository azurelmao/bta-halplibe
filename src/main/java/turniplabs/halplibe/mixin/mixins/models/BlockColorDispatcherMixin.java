package turniplabs.halplibe.mixin.mixins.models;

import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.util.dispatch.Dispatcher;
import net.minecraft.core.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.BlockBuilder;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Mixin(value = BlockColorDispatcher.class, remap = false)
public abstract class BlockColorDispatcherMixin extends Dispatcher<Block, BlockColor> {

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        try {
            Set<Map.Entry<Block, Function<Block, BlockColor>>> entries = ((Map<Block, Function<Block, BlockColor>>) BlockBuilder.class.getField("queuedBlockColors").get(null)).entrySet();
            for (Map.Entry<Block, Function<Block, BlockColor>> entry : entries){
                addDispatch(entry.getKey(),entry.getValue().apply(entry.getKey()));
            }
            BlockBuilder.class.getField("blockColorDispatcherInitialized").set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
