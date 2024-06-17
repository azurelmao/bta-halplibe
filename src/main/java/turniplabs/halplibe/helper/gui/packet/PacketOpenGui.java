package turniplabs.halplibe.helper.gui.packet;

import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOpenGui extends Packet {

    public String guiNamespace;
    public int windowId;

    public PacketOpenGui(@NotNull RegisteredGui gui, int windowId) {
        this.guiNamespace = gui.getNamespace();
        this.windowId = windowId;
    }

    public PacketOpenGui() {
    }


    @Override
    public void readPacketData(DataInputStream input) throws IOException {
        this.guiNamespace = input.readUTF();
        this.windowId = input.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream output) throws IOException {
        if(guiNamespace == null) throw new IOException("GUI Namespace can't be null!");
        output.writeUTF(guiNamespace);
        output.writeByte(windowId);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        RegisteredGui gui = GuiHelper.getGui(guiNamespace);
        if(gui == null) {
            HalpLibe.LOGGER.warn("Invalid gui id in packet: " + guiNamespace);
            return;
        }

        gui.handleOpenPacket(this);
    }

    @Override
    public int getPacketSize() {
        return guiNamespace.length() + 1;
    }
}
