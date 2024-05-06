package turniplabs.halplibe.mixin.mixins.registry;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.BlockBuilder;

@Mixin(value = Block.class, remap = false)
public abstract class BlockMixin {
	@Shadow public static int highestBlockId;
	
	@Shadow @Final public int id;

	@Shadow @Final public static Block[] blocksList;

	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void captureHighest(CallbackInfo ci) {
		BlockBuilder.Registry.highestVanilla = highestBlockId;
	}

	@Inject(at = @At(value = "INVOKE", target = "Ljava/lang/IllegalArgumentException;<init>(Ljava/lang/String;)V", shift = At.Shift.BEFORE), method = "<init>")
	public void addInfo(String key, int id, Material material, CallbackInfo ci) {
		throw new IllegalArgumentException("Slot " + id + " is already occupied by " + blocksList[id].getKey() + " when adding " + key);
	}

	@Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/achievement/stat/StatList;onBlockInit()V"))
	private static void delayInit() {
	}
}
