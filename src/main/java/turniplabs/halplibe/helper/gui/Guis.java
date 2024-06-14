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
import turniplabs.halplibe.helper.gui.factory.base.GuiFactory;
import turniplabs.halplibe.helper.gui.factory.block.BlockGuiFactory;
import turniplabs.halplibe.helper.gui.factory.block.TileGuiFactory;
import turniplabs.halplibe.helper.gui.factory.item.ItemGuiFactory;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

public final class Guis {

    /* ==============
        Client Guis
       ============== */

    public static final RegisteredGui EDIT_SIGN = GuiHelper.registerClientGui("minecraft", "edit_sign", new TileGuiFactory<TileEntitySign>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull TileEntitySign tile) {
            return new GuiEditSign(tile);
        }

        @Override
        public @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull TileEntitySign tile) {
            return null;
        }
    });

    public static final RegisteredGui EDIT_LABEL = GuiHelper.registerClientGui("minecraft", "edit_label", new ItemGuiFactory() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull ItemStack itemStack) {
            return new GuiEditLabel(itemStack, getSlot(player, itemStack));
        }

        @Override
        public @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull ItemStack itemStack) {
            return null;
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
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull TileEntityMobSpawner tile) {
            return new GuiPickMonster(tile.getMobId(), tile.x, tile.y, tile.z);
        }

        @Override
        public @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull TileEntityMobSpawner tile) {
            return null;
        }
    });

    public static final RegisteredGui WAND_MONSTER_PICKER = GuiHelper.registerClientGui("minecraft", "wand_monster_picker", new ItemGuiFactory() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull ItemStack itemStack) {
            String mobId = itemStack.getData().getStringOrDefault("monster", "Pig");
            return new GuiWandPickerMonster(mobId, itemStack);
        }

        @Override
        public @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull ItemStack itemStack) {
            return null;
        }
    });


    /* ==============
        Server Guis
       ============== */

    public static final RegisteredGui CHEST = GuiHelper.registerServerGui("minecraft", "chest", new TileGuiFactory<TileEntityChest>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull TileEntityChest tile) {
            return new GuiChest(player.inventory, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull TileEntityChest tile) {
            return new ContainerChest(player.inventory, tile);
        }
    });

    public static final RegisteredGui WORKBENCH = GuiHelper.registerServerGui("minecraft", "workbench", new BlockGuiFactory() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, int x, int y, int z) {
            return new GuiCrafting(player.inventory, player.world, x, y, z);
        }

        @Override
        public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, int x, int y, int z) {
            return new ContainerWorkbench(player.inventory, player.world, x, y, z);
        }
    });

    public static final RegisteredGui FURNACE = GuiHelper.registerServerGui("minecraft", "furnace", new TileGuiFactory<TileEntityFurnace>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull TileEntityFurnace tile) {
            return new GuiFurnace(player.inventory, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull TileEntityFurnace tile) {
            return new ContainerFurnace(player.inventory, tile);
        }
    });

    public static final RegisteredGui DISPENSER = GuiHelper.registerServerGui("minecraft", "dispenser", new TileGuiFactory<TileEntityDispenser>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull TileEntityDispenser tile) {
            return new GuiDispenser(player.inventory, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull TileEntityDispenser tile) {
            return new ContainerDispenser(player.inventory, tile);
        }
    });

    public static final RegisteredGui BLAST_FURNACE = GuiHelper.registerServerGui("minecraft", "blast_furnace", new TileGuiFactory<TileEntityFurnace>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull TileEntityFurnace tile) {
            return new GuiFurnace(player.inventory, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull TileEntityFurnace tile) {
            return new ContainerFurnace(player.inventory, tile);
        }
    });

    public static final RegisteredGui TROMMEL = GuiHelper.registerServerGui("minecraft", "trommel", new TileGuiFactory<TileEntityTrommel>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull TileEntityTrommel tile) {
            return new GuiTrommel(player.inventory, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull TileEntityTrommel tile) {
            return new ContainerTrommel(player.inventory, tile);
        }
    });

    public static final RegisteredGui PAINTING_PICKER = GuiHelper.registerServerGui("minecraft", "painting_picker", new GuiFactory() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player) {
            return new GuiPaintingPicker(player);
        }

        @Override
        public @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player) {
            return null;
        }
    });

    public static final RegisteredGui EDIT_FLAG = GuiHelper.registerServerGui("minecraft", "edit_flag", new TileGuiFactory<TileEntityFlag>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player, @NotNull TileEntityFlag tile) {
            return new GuiEditFlag(player, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player, @NotNull TileEntityFlag tile) {
            return new ContainerFlag(player.inventory, tile);
        }
    });
}
