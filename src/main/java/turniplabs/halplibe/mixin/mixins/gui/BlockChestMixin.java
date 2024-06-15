package turniplabs.halplibe.mixin.mixins.gui;

import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import turniplabs.halplibe.helper.gui.Guis;

@Mixin(
        value = BlockChest.class,
        remap = false
)
public abstract class BlockChestMixin extends BlockTileEntity {

    @Shadow public abstract void checkIfOtherHalfExists(World world, int x, int y, int z);

    public BlockChestMixin(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if(!world.isClientSide) checkIfOtherHalfExists(world, x, y, z);
        Guis.CHEST.open(player, x, y, z);
        return true;
    }
}
