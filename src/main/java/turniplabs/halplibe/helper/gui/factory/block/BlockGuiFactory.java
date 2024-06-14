package turniplabs.halplibe.helper.gui.factory.block;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.factory.IGuiFactory;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

public interface BlockGuiFactory extends IGuiFactory {

    @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, int x, int y, int z);

    @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, int x, int y, int z);

    @Override
    @NotNull
    default GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        if(y < 0) throwInvalidException("y value of block coordinates must be set to a positive value. This either means a wrong value has been used as an argument or that the wrong gui method has been called.");
        return createGui(gui, player, x, y, z);
    }

    @Override
    default @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        return createContainer(gui, player, x, y, z);
    }
}
