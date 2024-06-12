package turniplabs.halplibe.helper.gui.test;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.player.inventory.InventoryBasic;

public class TestTile extends TileEntity {

    private final InventoryBasic inv;

    public TestTile() {
        this.inv = new InventoryBasic("Test", 27);
    }

    public InventoryBasic getInv() {
        return inv;
    }
}
