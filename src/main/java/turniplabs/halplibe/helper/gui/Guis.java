package turniplabs.halplibe.helper.gui;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.*;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.gui.factory.ContainerGuiFactory;
import turniplabs.halplibe.helper.gui.factory.TileContainerGuiFactory;
import turniplabs.halplibe.helper.gui.factory.TileGuiFactory;
import turniplabs.halplibe.helper.gui.factory.base.GuiFactory;
import turniplabs.halplibe.helper.gui.packet.PacketOpenBlockGui;
import turniplabs.halplibe.helper.gui.packet.PacketOpenGui;

public final class Guis {

    /* ==============
        Client Guis
       ============== */

    public static final RegisteredGui EDIT_SIGN = GuiHelper.registerClientGui("minecraft", "edit_sign", new TileGuiFactory<TileEntitySign>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntitySign tile) {
            return new GuiEditSign(tile);
        }

        @Override
        public @NotNull PacketOpenBlockGui createGuiPacket(@NotNull RegisteredGui gui, int windowId, @NotNull EntityPlayerMP player, @Nullable ItemStack item, int x, int y, int z) {
            throw new AssertionError("Gui is client side!");
        }
    });

    public static final RegisteredGui EDIT_LABEL = GuiHelper.registerClientGui("minecraft", "edit_label", new GuiFactory() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, ItemStack itemStack, int x, int y, int z) {
            return new GuiEditLabel(itemStack, getSlot(player, itemStack));
        }

        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, ItemStack itemStack, @NotNull PacketOpenGui packet) {
            throw new AssertionError("Gui is client side!");
        }

        private int getSlot(EntityPlayer player, ItemStack itemStack) {
            for (int i = 0; i < player.inventory.mainInventory.length; ++i) {
                if (player.inventory.mainInventory[i] == itemStack) return i;
            }

            throw new AssertionError();
        }
    });

    public static final RegisteredGui MOB_SPAWNER_PICKER = GuiHelper.registerClientGui("minecraft", "mob_spawner_picker", new TileGuiFactory<TileEntityMobSpawner>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityMobSpawner tile) {
            return new GuiPickMonster(tile.getMobId(), tile.x, tile.y, tile.z);
        }

        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, @NotNull PacketOpenGui packet) {
            throw new AssertionError("Gui is client side!");
        }
    });

    public static final RegisteredGui WAND_MONSTER_PICKER = GuiHelper.registerClientGui("minecraft", "wand_monster_picker", new GuiFactory() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z) {
            String mobId = itemStack != null ? itemStack.getData().getStringOrDefault("monster", "Pig") : "Pig";
            return new GuiWandPickerMonster(mobId, itemStack);
        }

        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, @NotNull PacketOpenGui packet) {
            throw new AssertionError("Gui is client side!");
        }
    });


    /* ==============
        Server Guis
       ============== */

    public static final RegisteredGui CHEST = GuiHelper.registerServerGui("minecraft", "chest", new TileContainerGuiFactory<TileEntityChest>() {
        @Override
        public @NotNull Container createContainer(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityChest tile) {
            return new ContainerChest(player.inventory, tile);
        }

        @Override
        public @NotNull GuiScreen createGui(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityChest tile) {
            return new GuiChest(player.inventory, tile);
        }
    });

    public static final RegisteredGui WORKBENCH = GuiHelper.registerServerGui("minecraft", "workbench", new ContainerGuiFactory() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, ItemStack itemStack, int x, int y, int z) {
            return new GuiCrafting(player.inventory, player.world, x, y, z);
        }

        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, ItemStack itemStack, @NotNull PacketOpenGui packet) {
            PacketOpenBlockGui blockPacket = (PacketOpenBlockGui) packet;
            return new GuiCrafting(player.inventory, player.world, blockPacket.x, blockPacket.y, blockPacket.z);
        }

        @Override
        public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, ItemStack itemStack, int x, int y, int z) {
            return new ContainerWorkbench(player.inventory, player.world, x, y, z);
        }

        @Override
        public @NotNull PacketOpenGui createGuiPacket(@NotNull RegisteredGui gui, int windowId, @NotNull EntityPlayerMP player, ItemStack itemStack, int x, int y, int z) {
            return new PacketOpenBlockGui(gui, windowId, x, y, z);
        }
    });

    public static final RegisteredGui FURNACE = GuiHelper.registerServerGui("minecraft", "furnace", new TileContainerGuiFactory<TileEntityFurnace>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityFurnace tile) {
            return new GuiFurnace(player.inventory, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityFurnace tile) {
            return new ContainerFurnace(player.inventory, tile);
        }

    });

    public static final RegisteredGui DISPENSER = GuiHelper.registerServerGui("minecraft", "dispenser", new TileContainerGuiFactory<TileEntityDispenser>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityDispenser tile) {
            return new GuiDispenser(player.inventory, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityDispenser tile) {
            return new ContainerDispenser(player.inventory, tile);
        }
    });

    public static final RegisteredGui BLAST_FURNACE = GuiHelper.registerServerGui("minecraft", "blast_furnace", new TileContainerGuiFactory<TileEntityBlastFurnace>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityBlastFurnace tile) {
            return new GuiFurnace(player.inventory, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityBlastFurnace tile) {
            return new ContainerFurnace(player.inventory, tile);
        }
    });

    public static final RegisteredGui TROMMEL = GuiHelper.registerServerGui("minecraft", "trommel", new TileContainerGuiFactory<TileEntityTrommel>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityTrommel tile) {
            return new GuiTrommel(player.inventory, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TileEntityTrommel tile) {
            return new ContainerTrommel(player.inventory, tile);
        }
    });

    public static final RegisteredGui PAINTING_PICKER = GuiHelper.registerServerGui("minecraft", "painting_picker", new GuiFactory() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, int x, int y, int z) {
            return new GuiPaintingPicker(player);
        }

        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @Nullable ItemStack itemStack, @NotNull PacketOpenGui packet) {
            return new GuiPaintingPicker(player);
        }
    });

    public static final RegisteredGui EDIT_FLAG = GuiHelper.registerServerGui("minecraft", "edit_flag",
            (TileGuiFactory<TileEntityFlag>) (player, itemStack, tile) -> new GuiEditFlag(player, tile)
    );
}
