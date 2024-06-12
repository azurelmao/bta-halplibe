package turniplabs.halplibe.mixin.accessors;

import net.minecraft.core.achievement.stat.Stat;
import net.minecraft.core.achievement.stat.StatList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = StatList.class,remap = false)
public interface StatListAccessor {

    @Accessor
    static Map<Integer, Stat> getStatMap(){
        throw new AssertionError();
    }
}
