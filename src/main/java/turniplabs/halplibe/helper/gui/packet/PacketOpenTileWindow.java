package turniplabs.halplibe.helper.gui.packet;

import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet100OpenWindow;
import turniplabs.halplibe.helper.gui.RegisteredGui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOpenTileWindow extends Packet100OpenWindow {

    public int x;
    public int y;
    public int z;

    public PacketOpenTileWindow() {
    }

    public PacketOpenTileWindow(RegisteredGui gui, int windowId, int x, int y, int z) {
        super(windowId, gui.getInventoryType(), null, -1);
        this.x = x;
        this.y = y;
        this.z = z;
    }


    @Override
    public void readPacketData(DataInputStream dis) throws IOException {
        this.windowId = dis.readByte();
        this.inventoryType = dis.readByte();
        this.x = dis.readInt();
        this.y = dis.readInt();
        this.z = dis.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream dos) throws IOException {
        dos.writeByte(windowId);
        dos.writeByte(inventoryType);
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeInt(z);
    }

    @Override
    public int getPacketSize() {
        return 2 + 4 * 3;
    }
}
