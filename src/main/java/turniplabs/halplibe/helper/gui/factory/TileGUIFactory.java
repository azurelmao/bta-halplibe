package turniplabs.halplibe.helper.gui.factory;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.packet.Packet100OpenWindow;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.gui.RegisteredGui;
import turniplabs.halplibe.helper.gui.packet.PacketOpenTileWindow;

public interface TileGUIFactory<T extends TileEntity> extends ContainerGUIFactory {

    @NotNull Container createContainer(EntityPlayer player, T tile);

    @NotNull GuiScreen createGui(EntityPlayer player, T tile);

    @SuppressWarnings("unchecked")
    @Override
    default @NotNull Container createContainer(RegisteredGui gui, EntityPlayerMP player, int x, int y, int z) {
        return createContainer(player, (T) player.world.getBlockTileEntity(x, y, z));
    }

    @Override
    default @NotNull PacketOpenTileWindow createPacket(RegisteredGui gui, int windowId, EntityPlayerMP player, int x, int y, int z) {
        return new PacketOpenTileWindow(gui, windowId, x, y, z);
    }

    @SuppressWarnings("unchecked")
    @Override
    default @NotNull GuiScreen createGui(RegisteredGui gui, EntityPlayerSP player, int x, int y, int z) {
        return createGui(player, (T) getClientTile(player, x, y, z));
    }

    @SuppressWarnings("unchecked")
    @Override
    default @NotNull GuiScreen createGui(RegisteredGui gui, EntityPlayerSP player, Packet100OpenWindow packet) {
        PacketOpenTileWindow tilePacket = (PacketOpenTileWindow) packet;
        return createGui(player, (T) getClientTile(player, tilePacket.x, tilePacket.y, tilePacket.z));
    }


    default TileEntity getClientTile(EntityPlayerSP player, int x, int y, int z) {
        TileEntity tile = player.world.getBlockTileEntity(x, y, z);
        if (tile == null)
            throw new IllegalStateException("Client failed to find tile entity at x:%s, y:%s, z:%s. Please make sure your tile entity doesn't return null when `getDescriptionPacket` is called.");

        return tile;
    }

}
