package turniplabs.halplibe.helper.gui.factory;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.net.packet.Packet100OpenWindow;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.gui.RegisteredGui;

public interface GuiFactory {

    @NotNull Packet100OpenWindow createPacket(RegisteredGui gui, int windowId, EntityPlayerMP player, int x, int y, int z);

    @NotNull GuiScreen createGui(RegisteredGui gui, EntityPlayerSP player, int x, int y, int z);

    @NotNull GuiScreen createGui(RegisteredGui gui, EntityPlayerSP player, Packet100OpenWindow packet);

}
