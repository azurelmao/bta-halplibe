package turniplabs.halplibe.helper.gui.packet;

import turniplabs.halplibe.helper.gui.RegisteredGui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOpenBlockGui extends PacketOpenGui {

    public int x;
    public int y;
    public int z;

    public PacketOpenBlockGui(RegisteredGui gui, int windowId, int x, int y, int z) {
        super(gui, windowId);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PacketOpenBlockGui() {
    }


    @Override
    public void readPacketData(DataInputStream input) throws IOException {
        super.readPacketData(input);
        this.x = input.readInt();
        this.y = input.readInt();
        this.z = input.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream output) throws IOException {
        super.writePacketData(output);
        output.writeInt(x);
        output.writeInt(y);
        output.writeInt(z);
    }

    @Override
    public int getPacketSize() {
        return super.getPacketSize() + 3 * 4;
    }
}
