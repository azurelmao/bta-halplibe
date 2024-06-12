package turniplabs.halplibe.helper.gui.factory;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.RegisteredGui;
import turniplabs.halplibe.helper.gui.factory.base.GuiFactory;
import turniplabs.halplibe.helper.gui.factory.base.TileFactory;
import turniplabs.halplibe.helper.gui.packet.PacketOpenBlockGui;
import turniplabs.halplibe.helper.gui.packet.PacketOpenGui;

public interface TileGuiFactory<T extends TileEntity> extends GuiFactory, TileFactory {

    @NotNull GuiScreen createGui(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull T tile);

    @Override
    default @NotNull PacketOpenBlockGui createGuiPacket(@NotNull RegisteredGui gui, int windowId, @NotNull EntityPlayerMP player, @Nullable ItemStack item, int x, int y, int z) {
        return new PacketOpenBlockGui(gui, windowId, x, y, z);
    }

    @SuppressWarnings("unchecked")
    @Override
    default @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        return createGui(player, itemStack, (T) getClientTile(gui, player, itemStack, x, y, z));
    }

    @SuppressWarnings("unchecked")
    @Override
    default @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, @NotNull PacketOpenGui packet) {
        PacketOpenBlockGui tilePacket = (PacketOpenBlockGui) packet;
        return createGui(player, itemStack, (T) getClientTile(gui, player, itemStack, tilePacket.x, tilePacket.y, tilePacket.z));
    }
}
