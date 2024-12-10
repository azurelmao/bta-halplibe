package turniplabs.halplibe.mixin.accessors;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.IItemConvertible;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(value = Block.class, remap = false)
public interface BlockAccessor {
    @Accessor("statParent")
    Supplier<@NotNull IItemConvertible> getStatParent();
}
