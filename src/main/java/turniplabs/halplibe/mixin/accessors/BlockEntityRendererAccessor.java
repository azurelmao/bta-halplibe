package turniplabs.halplibe.mixin.accessors;

import net.minecraft.client.render.BlockEntityRenderDispatcher;
import net.minecraft.client.render.blockentity.BlockEntityRenderer;
import net.minecraft.core.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = BlockEntityRenderDispatcher.class, remap = false)
public interface BlockEntityRendererAccessor {
    @Accessor("renderers")
    Map<Class<? extends BlockEntity>, BlockEntityRenderer<?>> getSpecialRendererMap();

}
