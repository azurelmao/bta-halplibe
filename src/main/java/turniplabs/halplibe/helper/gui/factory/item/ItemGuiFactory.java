package turniplabs.halplibe.helper.gui.factory.item;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.factory.IGuiFactory;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

public interface ItemGuiFactory extends IGuiFactory {

    @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull ItemStack itemStack);

    @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull ItemStack itemStack);

    @SuppressWarnings("DataFlowIssue")
    @Override
    default @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        if(itemStack == null) throwInvalidException("Itemstack can't be null.");
        return createGui(gui, player, itemStack);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    default @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @Nullable ItemStack itemStack, int x, int y, int z) {
        if(itemStack == null) throwInvalidException("Itemstack can't be null.");
        return createContainer(gui, player, itemStack);
    }
}
