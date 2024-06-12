package turniplabs.halplibe.helper.gui.factory;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.RegisteredGui;
import turniplabs.halplibe.helper.gui.factory.base.ContainerFactory;
import turniplabs.halplibe.helper.gui.factory.base.TileFactory;

public interface TileContainerFactory<T extends TileEntity> extends ContainerFactory, TileFactory {

    @NotNull Container createContainer(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull T tile);

    @SuppressWarnings("unchecked")
    @Override
    default @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        return createContainer(player, itemStack, (T) player.world.getBlockTileEntity(x, y, z));
    }

}
