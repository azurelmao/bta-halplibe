package turniplabs.halplibe.mixin.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.HalpLibe;

@Debug(export = true)
@Mixin(
        value = GuiRenderItem.class,
        remap = false,
        priority = -1
)
public class GuiRenderItemMixin extends Gui {

    @Shadow
    Minecraft mc;

    @Inject(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V", at = @At("HEAD"))
    public void setDiscovered(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot, CallbackInfo ci) {
        if(mc.theWorld != null && mc.theWorld.getGameRule(HalpLibe.UNLOCK_ALL_RECIPES) && slot != null){
            slot.setDiscovered(true);
        }
    }


    @Inject(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/slot/Slot;getBackgroundIconIndex()I", shift = At.Shift.BEFORE))
    public void setDiscovered2(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot, CallbackInfo ci, @Local(ordinal = 2) LocalBooleanRef discovered) {
        if(mc.theWorld != null && mc.theWorld.getGameRule(HalpLibe.UNLOCK_ALL_RECIPES) && slot != null){
            slot.setDiscovered(true);
            discovered.set(true);
        }
    }
}
