package turniplabs.halplibe.helper.gui.packet;

import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.RegisteredGui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOpenGui extends Packet {

    public String namespace;
    public int windowId;

    public PacketOpenGui(RegisteredGui gui, int windowId) {
        this.namespace = gui.getNamespace();
        this.windowId = windowId;
    }

    public PacketOpenGui() {
    }


    @Override
    public void readPacketData(DataInputStream input) throws IOException {
        this.namespace = input.readUTF();
        this.windowId = input.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream output) throws IOException {
        output.writeUTF(namespace);
        output.writeByte(windowId);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        GuiHelper.getGui(namespace).openPacket(this);
    }

    @Override
    public int getPacketSize() {
        return namespace.length() + 1;
    }
}
