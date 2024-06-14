package turniplabs.halplibe.helper.gui.factory.base;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.factory.IGuiFactory;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

public interface GuiFactory extends IGuiFactory {

    @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player);

    @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player);

    @Override
    default @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        return createGui(gui, player);
    }

    @Override
    default @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        return createContainer(gui, player);
    }
}
