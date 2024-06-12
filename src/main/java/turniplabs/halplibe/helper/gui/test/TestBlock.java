package turniplabs.halplibe.helper.gui.test;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.RegisteredGui;
import turniplabs.halplibe.helper.gui.factory.TileGUIFactory;

public class TestBlock extends BlockTileEntity {

    private static final RegisteredGui GUI = GuiHelper.registerGui(HalpLibe.MOD_ID, new TileGUIFactory<TestTile>() {
        @Override
        public @NotNull Container createContainer(EntityPlayer player, TestTile tile) {
            return new TestContainer(player, tile);
        }

        @Override
        public @NotNull GuiScreen createGui(EntityPlayer player, TestTile tile) {
            return new TestGui(player, tile);
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
        GUI.open(player, x, y, z);
        return true;
    }
}
