package turniplabs.halplibe.mixin.mixins.gui;

import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.block.entity.TileEntityTrommel;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.Guis;

@Mixin(
        value = EntityPlayerMP.class,
        remap = false
)
public abstract class EntityPlayerMPMixin {

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIEditFlag(TileEntityFlag tile) {
        Guis.EDIT_FLAG.open(getPlayer(), null, tile.x, tile.y, tile.z);
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIChest(IInventory iinventory) {
        GuiHelper.reportVanillaGuiCall("BTA display gui method called");
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIWorkbench(int x, int y, int z) {
        Guis.WORKBENCH.open(getPlayer(), null, x, y, z);
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIPaintingPicker() {
        Guis.PAINTING_PICKER.open(getPlayer(), null, 0, 0, 0);
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIFurnace(TileEntityFurnace tile) {
        Guis.FURNACE.open(getPlayer(), null, tile.x, tile.y, tile.z);
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUITrommel(TileEntityTrommel tile) {
        Guis.TROMMEL.open(getPlayer(), null, tile.x, tile.y, tile.z);
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIDispenser(TileEntityDispenser tile) {
        Guis.DISPENSER.open(getPlayer(), null, tile.x, tile.y, tile.z);
    }


    @Unique
    private EntityPlayerMP getPlayer() {
        return (EntityPlayerMP) ((Object) this);
    }


}
