package turniplabs.halplibe.helper.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.packet.Packet100OpenWindow;
import net.minecraft.server.entity.player.EntityPlayerMP;
import turniplabs.halplibe.helper.gui.factory.ContainerGUIFactory;
import turniplabs.halplibe.helper.gui.factory.GuiFactory;

import java.util.Random;

public class RegisteredGui {

    private final String modId;
    private final int inventoryType;
    private final GuiFactory factory;

    public RegisteredGui(String modId, int guiType, GuiFactory factory) {
        this.modId = modId;
        this.inventoryType = guiType;
        this.factory = factory;
    }

    public void open(EntityPlayer player, int x, int y, int z) {
        if(player.world.isClientSide) return;

        if(player instanceof EntityPlayerSP) {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(factory.createGui(this, (EntityPlayerSP) player, x, y, z));
            return;
        }

        if(player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;

            int windowId = new Random().nextInt(100);
            playerMP.playerNetServerHandler.sendPacket(factory.createPacket(this, windowId, playerMP, x, y, z));

            if(factory instanceof ContainerGUIFactory) {
                player.craftingInventory = ((ContainerGUIFactory) factory).createContainer(this, playerMP, x, y, z);
                player.craftingInventory.windowId = windowId;
                player.craftingInventory.onContainerInit(playerMP);
            }

            return;
        }

        throw new AssertionError("This is not supposed to happen!");
    }

    public void openPacket(Packet100OpenWindow packet, Minecraft mc) {
        EntityPlayerSP player = mc.thePlayer;
        mc.displayGuiScreen(factory.createGui(this, player, packet));
        player.craftingInventory.windowId = packet.windowId;
    }


    public GuiFactory getFactory() {
        return factory;
    }

    public int getInventoryType() {
        return inventoryType;
    }

    public String getModId() {
        return modId;
    }
}
