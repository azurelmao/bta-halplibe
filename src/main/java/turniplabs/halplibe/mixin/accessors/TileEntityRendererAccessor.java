package turniplabs.halplibe.mixin.accessors;

import net.minecraft.client.render.BlockEntityRenderDispatcher;
import net.minecraft.client.render.blockentity.TileEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = BlockEntityRenderDispatcher.class, remap = false)
public interface TileEntityRendererAccessor {
    @Accessor("renderers")
    Map<Class<? extends TileEntity>, TileEntityRenderer<?>> getSpecialRendererMap();

}
