package turniplabs.halplibe.helper.gui.factory.base;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.item.ItemStack;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.RegisteredGui;
import turniplabs.halplibe.helper.gui.packet.PacketOpenGui;

public interface GuiFactory {

    @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z);

    @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, @NotNull PacketOpenGui packet);

    default @NotNull PacketOpenGui createGuiPacket(@NotNull RegisteredGui gui, int windowId, @NotNull EntityPlayerMP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        return new PacketOpenGui(gui, windowId);
    }

}
