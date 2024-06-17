package turniplabs.halplibe.helper.gui.registered;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityClientPlayerMP;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.util.helper.Listener;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.factory.IGuiFactory;
import turniplabs.halplibe.helper.gui.factory.base.GuiFactory;
import turniplabs.halplibe.helper.gui.factory.block.BlockGuiFactory;
import turniplabs.halplibe.helper.gui.factory.block.TileGuiFactory;
import turniplabs.halplibe.helper.gui.factory.item.ItemGuiFactory;
import turniplabs.halplibe.helper.gui.packet.PacketGuiButtonClick;
import turniplabs.halplibe.helper.gui.packet.PacketOpenGui;

import java.util.ArrayList;
import java.util.List;
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
            throw new IllegalArgumentException(String.format("Invalid namespace: %s. Must match the following regex '[a-zA-Z0-9_-]+'.", getNamespace()));

        if (GuiHelper.REGISTRY.getItem(getNamespace()) != null)
            throw new IllegalArgumentException(String.format("Duplicate namespace: %s.", getNamespace()));
    }

    public void open(@NotNull EntityPlayer player) {
        if (!(factory instanceof GuiFactory))
            throw new IllegalStateException("Gui is not an item gui!");

        open(player, null, 0, -100, 0);
    }

    public void open(@NotNull EntityPlayer player, @NotNull ItemStack itemStack) {
        if (!(factory instanceof ItemGuiFactory))
            throw new IllegalStateException("Gui is not an item gui!");

        open(player, itemStack, 0, -100, 0);
    }

    public void open(@NotNull EntityPlayer player, int x, int y, int z) {
        if (!(factory instanceof BlockGuiFactory))
            throw new IllegalStateException("Gui is not a block gui!");

        open(player, null, x, y, z);
    }

    public void open(@NotNull EntityPlayer player, TileEntity tile) {
        if (!(factory instanceof TileGuiFactory))
            throw new IllegalStateException("Gui is not a tile gui!");

        open(player, null, tile.x, tile.y, tile.z);
    }

    protected void open(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, int x, int y, int z) {
        if (player.world.isClientSide && serverSide) return;

        if (player instanceof EntityPlayerSP) {
            EntityPlayerSP playerSP = (EntityPlayerSP) player;
            GuiScreen gui = factory.createGui(this, playerSP, itemStack, x, y, z);
            Minecraft.getMinecraft(RegisteredGui.class).displayGuiScreen(addButtonListeners(gui, playerSP));
            return;
        }

        if (player instanceof EntityPlayerMP) {
            if (!serverSide) return;
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

    private GuiScreen addButtonListeners(GuiScreen gui, EntityPlayerSP playerSP) {
        List<Integer> ids = new ArrayList<>(gui.controlList.size());
        for (GuiButton button : gui.controlList) {
            if (ids.contains(button.id))
                throw new IllegalStateException(String.format("Found button in gui '%s' with duplicate id: '%s'.", gui, button.id));

            final Listener<GuiButton> oldListener = button.listener;
            button.setListener(aButton -> {
                if (playerSP instanceof EntityClientPlayerMP) {
                    EntityClientPlayerMP playerMP = (EntityClientPlayerMP) playerSP;
                    playerMP.sendQueue.addToSendQueue(new PacketGuiButtonClick(getNamespace(), aButton.id));
                }

                factory.onButtonPress(this, playerSP, aButton.id);
                if(oldListener != null) oldListener.listen(aButton);
            });

            ids.add(button.id);
        }

        return gui;
    }

    @ApiStatus.Internal
    public void handleOpenPacket(@NotNull PacketOpenGui packet) {
        Minecraft mc = Minecraft.getMinecraft(RegisteredGui.class);
        EntityPlayerSP player = mc.thePlayer;
        GuiScreen gui = factory.createGui(this, player, packet);

        mc.displayGuiScreen(addButtonListeners(gui, player));
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
