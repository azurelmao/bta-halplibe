package turniplabs.halplibe.mixin;

import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobCreeper;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.SpecialMobDropHelper;

@Mixin(value = MobCreeper.class,remap = false)
public abstract class MobCreeperMixin extends MobMonster {

    private MobCreeperMixin(@Nullable World world) {
        super(world);
    }

    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/monster/MobCreeper;dropItem(II)Lnet/minecraft/core/entity/EntityItem;",shift = At.Shift.AFTER))
    public void dropItemsOnDeath(Entity entityKilledBy, CallbackInfo ci) {
        for (WeightedRandomLootObject entry : SpecialMobDropHelper.Creeper.getDrops().getEntries()) {
            ItemStack stack = entry.getItemStack();
            if(stack != null){
                dropItem(stack,0.0f);
            }
        }
    }
}
