package turniplabs.halplibe.helper.recipeBuilders.modifiers;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import turniplabs.halplibe.helper.RecipeBuilder;

public class WorkbenchModifier {
    protected RecipeGroup<RecipeEntryCrafting<?, ?>> recipeGroup;
    @SuppressWarnings({"unchecked", "unused"})
    public WorkbenchModifier(String namespace){
        recipeGroup = (RecipeGroup<RecipeEntryCrafting<?, ?>>) RecipeBuilder.getRecipeGroup(namespace, "workbench", new RecipeSymbol(Blocks.WORKBENCH.getDefaultStack()));
    }
    @SuppressWarnings({"unchecked", "unused"})
    public WorkbenchModifier removeRecipe(String recipeID){
        recipeGroup.unregister(recipeID);
        return this;
    }
}
