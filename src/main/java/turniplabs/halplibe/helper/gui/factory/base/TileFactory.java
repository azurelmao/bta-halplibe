package turniplabs.halplibe.helper.gui.factory.base;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.RegisteredGui;

public interface TileFactory {

    default TileEntity getClientTile(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        TileEntity tile = player.world.getBlockTileEntity(x, y, z);
        if (tile == null)
            throw new IllegalStateException(String.format(
                    "Client failed to find tile entity to create gui '%s' at x:%s, y:%s, z:%s. " +
                            "Please make sure your tile entity doesn't return null when `getDescriptionPacket` is called.",
                    gui.getNamespace(), x, y, z
            ));

        return tile;
    }

}
