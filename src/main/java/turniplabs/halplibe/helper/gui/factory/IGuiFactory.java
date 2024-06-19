package turniplabs.halplibe.helper.gui.factory;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.entity.player.EntityPlayer;
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

public interface IGuiFactory {

    /**
     * Called to create a gui screen on a client.
     *
     * @param gui the registered gui that needs to create a gui
     * @param player the player that will see the gui
     * @param itemStack an item, in case the gui is an item gui
     * @param x x coordinate, in case the gui is a block gui
     * @param y y coordinate, in case the gui is a block gui
     * @param z z coordinate, in case the gui is a block gui
     * @return a new gui
     */
    @ApiStatus.Internal
    @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z);

    /**
     * Called to create a gui container on a server.
     *
     * @param gui the registered gui that needs to create a container
     * @param player the player that will open the container
     * @param itemStack an item, in case the gui is an item gui
     * @param x x coordinate, in case the gui is a block gui
     * @param y y coordinate, in case the gui is a block gui
     * @param z z coordinate, in case the gui is a block gui
     * @return a new container
     */
    @ApiStatus.Internal
    @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @Nullable ItemStack itemStack, int x, int y, int z);

    /**
     * Called when a gui interaction happens.<p>
     * The player and clicker parameter are not the same when a client acknowledges
     * that another player has pressed a button.<p>
     * As an example, if player1 clicked a button on a server,
     * this method will be called on player2's client
     * with player2 as the player argument and player1 as the clicker argument
     *
     * @param gui the registered gui that was clicked
     * @param player the player that acknowledges that a player has pressed a button
     * @param clicker the player that clicked the button
     * @param buttonId the id of the button
     */
    default void onButtonClick(@NotNull RegisteredGui gui, @NotNull EntityPlayer player, @NotNull EntityPlayer clicker, int buttonId) {

    }

    @ApiStatus.Internal
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

    @ApiStatus.Internal
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

    @ApiStatus.Internal
    default void throwInvalidException(String message) {
        throw new IllegalArgumentException(String.format("Invalid arguments for gui factory '%s': %s", getClass(), message));
    }

}
