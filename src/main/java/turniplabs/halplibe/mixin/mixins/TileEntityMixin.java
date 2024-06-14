package turniplabs.halplibe.mixin.mixins;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = TileEntity.class,
        remap = false
)
public abstract class TileEntityMixin {

    @Inject(
            method = "getDescriptionPacket",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void injectGetDescriptionPacket(CallbackInfoReturnable<Packet> cir) {
        cir.setReturnValue(new Packet140TileEntityData((TileEntity) ((Object)this)));
    }
}
