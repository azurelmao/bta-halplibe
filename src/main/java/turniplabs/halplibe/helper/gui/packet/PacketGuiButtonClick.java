package turniplabs.halplibe.helper.gui.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;
import turniplabs.halplibe.util.PlayerUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketGuiButtonClick extends Packet {

    public String guiNamespace;
    public int buttonId;
    public int clickerId;

    public PacketGuiButtonClick(String guiNamespace, int buttonId, int clickerId) {
        this.guiNamespace = guiNamespace;
        this.buttonId = buttonId;
        this.clickerId = clickerId;
    }

    public PacketGuiButtonClick(String guiNamespace, int buttonId) {
        this(guiNamespace, buttonId, -1);
    }

    public PacketGuiButtonClick() {
    }


    @Override
    public void readPacketData(DataInputStream input) throws IOException {
        this.guiNamespace = input.readUTF();
        this.buttonId = input.readInt();
        this.clickerId = input.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream output) throws IOException {
        if(guiNamespace == null) throw new IOException("GUI Namespace can't be null!");
        output.writeUTF(guiNamespace);
        output.writeInt(buttonId);
        output.writeInt(clickerId);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        RegisteredGui gui = GuiHelper.getGui(guiNamespace);
        if(gui == null) {
            HalpLibe.LOGGER.warn("Invalid gui id in packet: " + guiNamespace);
            return;
        }

        EntityPlayer player = PlayerUtils.getPlayer(netHandler);
        EntityPlayer clicker = null;
        if(player.craftingInventory == null) return;

        if(player instanceof EntityPlayerMP) {
            Packet packet = new PacketGuiButtonClick(guiNamespace, buttonId, player.id);
            clicker = player;

            for (EntityPlayerMP other : MinecraftServer.getInstance().playerList.playerEntities) {
                if(other == player) continue;
                if(other.craftingInventory == null || other.craftingInventory.windowId != player.craftingInventory.windowId) continue;
                other.playerNetServerHandler.sendPacket(packet);
            }
        }

        if(player instanceof EntityPlayerSP) {
            WorldClient world = (WorldClient) Minecraft.getMinecraft(this).theWorld;
            Entity entity = world.getEntityFromId(clickerId);
            if(entity instanceof EntityPlayer) clicker = (EntityPlayer) entity;
        }

        if(clicker != null) gui.getFactory().onButtonClick(gui, player, clicker, buttonId);
    }

    @Override
    public int getPacketSize() {
        return guiNamespace.length() + 4 * 2;
    }
}
