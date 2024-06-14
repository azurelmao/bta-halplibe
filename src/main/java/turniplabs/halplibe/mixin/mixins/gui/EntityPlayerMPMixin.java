package turniplabs.halplibe.mixin.mixins.gui;

import net.minecraft.core.block.entity.*;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
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
        Guis.EDIT_FLAG.open(getPlayer(), tile.x, tile.y, tile.z);
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
        Guis.WORKBENCH.open(getPlayer(), x, y, z);
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIPaintingPicker() {
        Guis.PAINTING_PICKER.open(getPlayer());
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIFurnace(TileEntityFurnace tile) {
        Guis.FURNACE.open(getPlayer(), tile.x, tile.y, tile.z);
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUITrommel(TileEntityTrommel tile) {
        Guis.TROMMEL.open(getPlayer(), tile.x, tile.y, tile.z);
    }

    /**
     * @author kill05
     * @reason gui rewrite
     */
    @Overwrite
    public void displayGUIDispenser(TileEntityDispenser tile) {
        Guis.DISPENSER.open(getPlayer(), tile.x, tile.y, tile.z);
    }


    @Redirect(
            method = "getTileEntityInfo",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/entity/TileEntity;getDescriptionPacket()Lnet/minecraft/core/net/packet/Packet;")
    )
    public Packet redirectUpdateTileEntity(TileEntity tile) {
        Packet packet = tile.getDescriptionPacket();
        if(packet == null) throw new IllegalStateException(String.format("Tile entity '%s' returned null description packet.", tile));
        return packet;
    }


    @Unique
    private EntityPlayerMP getPlayer() {
        return (EntityPlayerMP) ((Object) this);
    }


}
