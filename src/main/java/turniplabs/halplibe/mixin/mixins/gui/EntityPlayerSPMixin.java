package turniplabs.halplibe.mixin.mixins.gui;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.Guis;

@Mixin(
        value = EntityPlayerSP.class,
        remap = false
)
public abstract class EntityPlayerSPMixin {

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIEditSign(TileEntitySign tile) {
        Guis.EDIT_SIGN.open(getPlayer(), null, tile.x, tile.y, tile.z);
    }

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
    public void displayGUIEditLabel(ItemStack itemstack, int slot) {
        Guis.EDIT_LABEL.open(getPlayer(), itemstack, 0, 0, 0);
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
    public void displayGUIMobSpawnerPicker(int x, int y, int z) {
        Guis.MOB_SPAWNER_PICKER.open(getPlayer(), null, x, y, z);
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIWandMonsterPicker(ItemStack stack) {
        Guis.WAND_MONSTER_PICKER.open(getPlayer(), stack, 0, 0, 0);
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
    private EntityPlayerSP getPlayer() {
        return (EntityPlayerSP) ((Object) this);
    }

}
