package turniplabs.halplibe.helper.gui.factory;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.packet.PacketOpenBlockGui;
import turniplabs.halplibe.helper.gui.packet.PacketOpenGui;
import turniplabs.halplibe.helper.gui.packet.PacketOpenItemGui;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

@ApiStatus.Internal
public interface IGuiFactory {

    @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z);

    @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @Nullable ItemStack itemStack, int x, int y, int z);

    default @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull PacketOpenGui packet) {
        if(!gui.isServerSide()) throw new IllegalStateException("Gui is client side!");

        if(packet instanceof PacketOpenBlockGui) {
            PacketOpenBlockGui blockPacket = (PacketOpenBlockGui) packet;
            return createGui(gui, player, null, blockPacket.x, blockPacket.y, blockPacket.z);
        }

        if(packet instanceof PacketOpenItemGui) {
            return createGui(gui, player, ((PacketOpenItemGui) packet).itemStack, 0, -100, 0);
        }

        return createGui(gui, player, null, 0, -100, 0);
    }

    default @NotNull PacketOpenGui createGuiPacket(@NotNull RegisteredGui gui, int windowId, @NotNull EntityPlayerMP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        if(!gui.isServerSide()) throw new IllegalStateException("Gui is client side!");

        if(itemStack != null) {
            return new PacketOpenItemGui(gui, windowId, itemStack);
        }

        if(y >= 0) {
            return new PacketOpenBlockGui(gui, windowId, x, y, z);
        }

        return new PacketOpenGui(gui, windowId);
    }

    default void throwInvalidException(String message) {
        throw new IllegalArgumentException(String.format("Invalid arguments for gui factory '%s': %s", getClass(), message));
    }

}
