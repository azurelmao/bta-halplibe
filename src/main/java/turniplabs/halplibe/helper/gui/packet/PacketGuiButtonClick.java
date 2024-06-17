package turniplabs.halplibe.helper.gui.packet;

import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.apache.commons.lang3.reflect.FieldUtils;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketGuiButtonClick extends Packet {

    public String guiNamespace;
    public int id;

    public PacketGuiButtonClick(String guiNamespace, int id) {
        this.guiNamespace = guiNamespace;
        this.id = id;
    }

    public PacketGuiButtonClick() {
    }


    @Override
    public void readPacketData(DataInputStream input) throws IOException {
        this.guiNamespace = input.readUTF();
        this.id = input.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream output) throws IOException {
        if(guiNamespace == null) throw new IOException("GUI Namespace can't be null!");
        output.writeUTF(guiNamespace);
        output.writeInt(id);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        try {
            EntityPlayerMP playerMP = (EntityPlayerMP) FieldUtils.readField(netHandler, "playerEntity", true);
            RegisteredGui gui = GuiHelper.getGui(guiNamespace);
            gui.getFactory().onButtonPress(gui, playerMP, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getPacketSize() {
        return guiNamespace.length() + 4;
    }
}
