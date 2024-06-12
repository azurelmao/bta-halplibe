package turniplabs.halplibe.helper.gui.test;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.RegisteredGui;
import turniplabs.halplibe.helper.gui.factory.TileContainerGuiFactory;

public class TestBlock extends BlockTileEntity {

    private static final RegisteredGui GUI = GuiHelper.registerServerGui(HalpLibe.MOD_ID, "test", new TileContainerGuiFactory<TestTile>() {
        @Override
        public @NotNull GuiScreen createGui(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TestTile tile) {
            return new TestGui(player, tile);
        }

        @Override
        public @NotNull Container createContainer(@NotNull EntityPlayer player, @Nullable ItemStack itemStack, @NotNull TestTile tile) {
            return new TestContainer(player, tile);
        }
    });

    public TestBlock() {
        super("test", 3000, Material.wood);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TestTile();
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        GUI.open(player, null, x, y, z);
        return true;
    }
}
