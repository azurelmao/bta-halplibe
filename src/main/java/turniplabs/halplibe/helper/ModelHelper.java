package turniplabs.halplibe.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModelHelper {

    public static Map<Class<?>, EntityRenderer<?>> entityRenderers;
    public static Map<Class<?>, TileEntityRenderer<?>> tileEntityRenderers;

    public static void setBlockModel(@NotNull Block<? extends BlockLogic> block, @NotNull Supplier<BlockModel<?>> func) {
        BlockModelDispatcher.getInstance().addDispatch(block,func.get());
    }

    public static void setBlockColor(@NotNull Block<? extends BlockLogic> block, @NotNull Supplier<BlockColor> func) {
        BlockColorDispatcher.getInstance().addDispatch(block,func.get());
    }

    public static void setItemModel(@NotNull Item item, @NotNull Supplier<ItemModel> func) {
        ItemModelDispatcher.getInstance().addDispatch(item,func.get());
    }

    public static void setEntityModel(@NotNull Class<? extends Entity> entity, @NotNull Supplier<EntityRenderer<?>> func) {
        entityRenderers.put(entity, func.get());
    }

    public static void setTileEntityModel(@NotNull Class<? extends TileEntity> tileEntity, @NotNull Supplier<TileEntityRenderer<?>> func) {
        tileEntityRenderers.put(tileEntity, func.get());
    }

}
