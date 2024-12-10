package turniplabs.halplibe.helper.recipeBuilders.modifiers;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryBlastFurnace;
import turniplabs.halplibe.helper.RecipeBuilder;

public class BlastFurnaceModifier {
    protected RecipeGroup<RecipeEntryBlastFurnace> recipeGroup;
    @SuppressWarnings("unchecked")
    public BlastFurnaceModifier(String namespace){
        recipeGroup = (RecipeGroup<RecipeEntryBlastFurnace>) RecipeBuilder.getRecipeGroup(namespace, "blast_furnace", new RecipeSymbol(Blocks.FURNACE_BLAST_ACTIVE.getDefaultStack()));
    }
    @SuppressWarnings({"unchecked", "unused"})
    public BlastFurnaceModifier removeRecipe(String recipeID){
        recipeGroup.unregister(recipeID);
        return this;
    }
}
