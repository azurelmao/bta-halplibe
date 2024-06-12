package turniplabs.halplibe.helper.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.factory.base.ContainerFactory;
import turniplabs.halplibe.helper.gui.factory.base.GuiFactory;
import turniplabs.halplibe.helper.gui.packet.PacketOpenGui;

import java.util.Random;
import java.util.regex.Pattern;

public class RegisteredGui {

    private static final Random RANDOM = new Random();
    private static final Pattern ID_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");

    private final String modId;
    private final String id;
    private final GuiFactory factory;
    private final boolean serverSide;

    public RegisteredGui(@NotNull String modId, @NotNull String id, @NotNull GuiFactory factory, boolean serverSide) {
        this.modId = modId;
        this.id = id;
        this.factory = factory;
        this.serverSide = serverSide;

        if (!ID_PATTERN.matcher(id).matches())
            throw new IllegalArgumentException(String.format("Invalid id: %s. Must match the following regex '[a-zA-Z0-9_-]+'.", id));

        if(GuiHelper.REGISTRY.getItem(getNamespace()) != null)
            throw new IllegalArgumentException(String.format("Duplicate namespace: %s.", getNamespace()));
    }

    public void open(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, int x, int y, int z) {
        if (player.world.isClientSide && serverSide) return;

        if (player instanceof EntityPlayerSP) {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(factory.createGui(this, (EntityPlayerSP) player, itemStack, x, y, z));
            return;
        }

        if (player instanceof EntityPlayerMP) {
            if(!serverSide) return;
            EntityPlayerMP playerMP = (EntityPlayerMP) player;

            int windowId = RANDOM.nextInt(100);
            playerMP.playerNetServerHandler.sendPacket(factory.createGuiPacket(this, windowId, playerMP, itemStack, x, y, z));

            if (factory instanceof ContainerFactory) {
                player.craftingInventory = ((ContainerFactory) factory).createContainer(this, playerMP, itemStack, x, y, z);
                player.craftingInventory.windowId = windowId;
                player.craftingInventory.onContainerInit(playerMP);
            }

            return;
        }

        throw new AssertionError("This is not supposed to happen!");
    }

    public void openPacket(@NotNull PacketOpenGui packet) {
        Minecraft mc = Minecraft.getMinecraft(RegisteredGui.class);
        EntityPlayerSP player = mc.thePlayer;
        mc.displayGuiScreen(factory.createGui(this, player, player.inventory.getCurrentItem(), packet));
        player.craftingInventory.windowId = packet.windowId;
    }


    public String getModId() {
        return modId;
    }

    public String getId() {
        return id;
    }

    public String getNamespace() {
        return modId + ":" + id;
    }

    public GuiFactory getFactory() {
        return factory;
    }

}
