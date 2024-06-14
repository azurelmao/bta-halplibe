package turniplabs.halplibe.helper.gui.factory.block;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

public interface TileGuiFactory<T extends TileEntity> extends BlockGuiFactory {

    @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull T tile);

    @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull T tile);

    @Override
    default @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, int x, int y, int z) {
        return createGui(gui, player, getTile(gui, player, x, y, z));
    }

    @Override
    default @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, int x, int y, int z) {
        return createContainer(gui, player, getTile(gui, player, x, y, z));
    }

    @SuppressWarnings("unchecked")
    default T getTile(@NotNull RegisteredGui gui, @NotNull EntityPlayer player, int x, int y, int z) {
        TileEntity tile = player.world.getBlockTileEntity(x, y, z);
        if (tile == null)
            throw new IllegalStateException(String.format("Failed to find tile entity to create gui '%s' at x:%s, y:%s, z:%s", gui.getNamespace(), x, y, z));

        try {
            return (T) tile;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Found wrong type of tile entity.", e);
        }
    }


}
