package turniplabs.halplibe.mixin.mixins;

import net.minecraft.core.achievement.stat.Stat;
import net.minecraft.core.achievement.stat.StatList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import turniplabs.halplibe.mixin.accessors.StatListAccessor;

@Mixin(value = Stat.class,remap = false)
public class StatMixin{

    @Shadow @Final public int statId;

    @Shadow @Final String statName;

    @Inject(method = "registerStat",at = @At("HEAD"), cancellable = true)
    public void registerStat(CallbackInfoReturnable<Stat> cir) {
        if (StatListAccessor.getStatMap().containsKey(this.statId)) {
            System.err.println("Duplicate stat id: \"" + (StatListAccessor.getStatMap().get(this.statId)).getStatName() + "\" and \"" + this.statName + "\" at id " + this.statId);
            cir.setReturnValue(StatListAccessor.getStatMap().get(this.statId));
        }
    }
}
