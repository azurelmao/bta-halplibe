package turniplabs.halplibe.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;

@Environment(EnvType.CLIENT)
public interface ModelEntrypoint {
    void initBlockModels(BlockModelDispatcher dispatcher);
    void initItemModels(ItemModelDispatcher dispatcher);
    void initEntityModels(EntityRenderDispatcher dispatcher);
    void initTileEntityModels(TileEntityRenderDispatcher dispatcher);
    void initBlockColors(BlockColorDispatcher dispatcher);
}
