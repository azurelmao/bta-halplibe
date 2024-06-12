package turniplabs.halplibe.helper.gui.test;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class TestContainer extends Container {

    private final TestTile tile;

    public TestContainer(EntityPlayer player, TestTile tile) {
        this.tile = tile;
        int i = (3 - 4) * 18;
        for (int j = 0; j < 3; ++j) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(tile.getInv(), i1 + j * 9, 8 + i1 * 18, 18 + j * 18));
            }
        }
        for (int k = 0; k < 3; ++k) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(player.inventory, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
            }
        }
        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(player.inventory, l, 8 + l * 18, 161 + i));
        }
    }

    public TestTile getTile() {
        return tile;
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

}
