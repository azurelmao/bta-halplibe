package turniplabs.halplibe.mixin.accessors;

import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RecipeSymbol.class, remap = false)
public interface RecipeSymbolAccessor {
    @Accessor
    void setSymbol(char symbol);
}
