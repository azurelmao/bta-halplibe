package turniplabs.halplibe.helper.gui.factory;

import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.gui.RegisteredGui;

public interface ContainerGUIFactory extends GuiFactory {

    @NotNull Container createContainer(RegisteredGui gui, EntityPlayerMP player, int x, int y, int z);

}
