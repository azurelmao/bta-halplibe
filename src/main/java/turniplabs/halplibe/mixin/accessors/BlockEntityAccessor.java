package turniplabs.halplibe.mixin.accessors;

import net.minecraft.core.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = BlockEntity.class, remap = false)
public interface BlockEntityAccessor {
    @Invoker("addMapping")
    static void callAddMapping(Class<?> clazz, String s) {
        throw new AssertionError();
    }
}
