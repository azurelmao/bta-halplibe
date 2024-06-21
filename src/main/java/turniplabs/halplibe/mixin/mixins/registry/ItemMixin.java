package turniplabs.halplibe.mixin.mixins.registry;

import net.minecraft.core.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.ItemHelper;

@Mixin(value = Item.class, remap = false)
public class ItemMixin {
	@Shadow public static int highestItemId;

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/core/item/Item;highestItemId:I", shift = At.Shift.BEFORE), method = "<clinit>", cancellable = true)
	private static void captureHighest(CallbackInfo ci) {
		ItemHelper.Registry.highestVanilla = highestItemId;
		ci.cancel();
	}

	@Redirect(method = "initializeItems()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/achievement/stat/StatList;onItemInit()V"))
	private static void delayInit() {
	}
}
