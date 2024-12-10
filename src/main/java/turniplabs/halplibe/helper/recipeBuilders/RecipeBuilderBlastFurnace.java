package turniplabs.halplibe.helper.recipeBuilders;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryBlastFurnace;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeBuilder;

public class RecipeBuilderBlastFurnace extends RecipeBuilderFurnace{
    /**
     * Used for creating new blast furnace recipes.
     * @param modID Namespace to create recipe under
     */
    public RecipeBuilderBlastFurnace(String modID) {
        super(modID);
    }
    @Override
    @SuppressWarnings({"unchecked", "unused"})
    public void create(String recipeID, ItemStack outputStack) {
        ((RecipeGroup< RecipeEntryBlastFurnace>) RecipeBuilder.getRecipeGroup(modID, "blast_furnace", new RecipeSymbol(Blocks.FURNACE_BLAST_ACTIVE.getDefaultStack())))
                .register(recipeID, new RecipeEntryBlastFurnace(input, outputStack));
    }
}
