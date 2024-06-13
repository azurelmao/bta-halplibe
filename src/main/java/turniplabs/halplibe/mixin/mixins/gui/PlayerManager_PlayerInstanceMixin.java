package turniplabs.halplibe.mixin.mixins.gui;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
        targets = "net.minecraft.server.player.PlayerManager$PlayerInstance",
        remap = false
)
public abstract class PlayerManager_PlayerInstanceMixin {

    @Redirect(
            method = "updateTileEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/entity/TileEntity;getDescriptionPacket()Lnet/minecraft/core/net/packet/Packet;")
    )
    public Packet redirectUpdateTileEntity(TileEntity tile) {
        Packet packet = tile.getDescriptionPacket();
        if(packet == null) throw new IllegalStateException(String.format("Tile entity '%s' returned null description packet.", tile));
        return packet;
    }
}
