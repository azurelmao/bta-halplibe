package turniplabs.halplibe.helper.recipeBuilders.modifiers;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import turniplabs.halplibe.helper.RecipeBuilder;


public class FurnaceModifier {
    protected RecipeGroup<RecipeEntryFurnace> recipeGroup;
    @SuppressWarnings("unchecked")
    public FurnaceModifier(String namespace){
        recipeGroup = (RecipeGroup<RecipeEntryFurnace>) RecipeBuilder.getRecipeGroup(namespace, "furnace", new RecipeSymbol(Blocks.FURNACE_STONE_ACTIVE.getDefaultStack()));
    }
    @SuppressWarnings({"unchecked", "unused"})
    public FurnaceModifier removeRecipe(String recipeID){
        recipeGroup.unregister(recipeID);
        return this;
    }
}
