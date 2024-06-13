package turniplabs.halplibe.helper.gui.registered;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.factory.IGuiFactory;
import turniplabs.halplibe.helper.gui.factory.base.GuiFactory;
import turniplabs.halplibe.helper.gui.factory.block.BlockGuiFactory;
import turniplabs.halplibe.helper.gui.factory.item.ItemGuiFactory;
import turniplabs.halplibe.helper.gui.packet.PacketOpenGui;

import java.util.Random;
import java.util.regex.Pattern;

public class RegisteredGui {

    private static final Random RANDOM = new Random();
    private static final Pattern ID_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");

    private final String modId;
    private final String id;
    private final IGuiFactory factory;
    private final boolean serverSide;

    public RegisteredGui(@NotNull String modId, @NotNull String id, @NotNull IGuiFactory factory, boolean serverSide) {
        this.modId = modId;
        this.id = id;
        this.factory = factory;
        this.serverSide = serverSide;

        if (!ID_PATTERN.matcher(modId + id).matches())
            throw new IllegalArgumentException(String.format("Invalid namespace:%s. Must match the following regex '[a-zA-Z0-9_-]+'.", getNamespace()));

        if(GuiHelper.REGISTRY.getItem(getNamespace()) != null)
            throw new IllegalArgumentException(String.format("Duplicate namespace: %s.", getNamespace()));
    }

    public void open(@NotNull EntityPlayer player) {
        if(!(factory instanceof GuiFactory))
            throw new IllegalStateException("Gui is not an item gui!");

        open(player, null, 0, -100, 0);
    }

    public void open(@NotNull EntityPlayer player, @NotNull ItemStack itemStack) {
        if(!(factory instanceof ItemGuiFactory))
            throw new IllegalStateException("Gui is not an item gui!");

        open(player, itemStack, 0, -100, 0);
    }

    public void open(@NotNull EntityPlayer player, int x, int y, int z) {
        if(!(factory instanceof BlockGuiFactory))
            throw new IllegalStateException("Gui is not a block gui!");

        open(player, null, x, y, z);
    }

    protected void open(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, int x, int y, int z) {
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
            Container container = factory.createContainer(this, playerMP, itemStack, x, y, z);

            if (container != null) {
                player.craftingInventory = container;
                player.craftingInventory.windowId = windowId;
                player.craftingInventory.onContainerInit(playerMP);
            }

            return;
        }

        HalpLibe.LOGGER.warn("Tried to open a gui for invalid EntityPlayer: " + player);
    }

    public void openPacket(@NotNull PacketOpenGui packet) {
        Minecraft mc = Minecraft.getMinecraft(RegisteredGui.class);
        EntityPlayerSP player = mc.thePlayer;
        mc.displayGuiScreen(factory.createGui(this, player, packet));
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

    public IGuiFactory getFactory() {
        return factory;
    }

    public boolean isServerSide() {
        return serverSide;
    }
}
