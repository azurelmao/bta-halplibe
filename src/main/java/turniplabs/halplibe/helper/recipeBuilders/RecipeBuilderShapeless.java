package turniplabs.halplibe.helper.recipeBuilders;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeBuilder;

import java.util.ArrayList;
import java.util.List;

public class RecipeBuilderShapeless extends RecipeBuilderBase{
    private final List<RecipeSymbol> symbolShapelessList = new ArrayList<>();
    /**
     * Used for creating new shapeless workbench recipes.
     * @param modID Namespace to create recipe under
     */
    public RecipeBuilderShapeless(String modID) {
        super(modID);
    }

    /**
     * @param stack Item to add to recipe's item list
     * @return Copy of {@link RecipeBuilderShapeless}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShapeless addInput(IItemConvertible stack){
        return addInput(stack, 0);
    }

    /**
     * @param stack Item to add to recipe's item list
     * @param meta Required meta of the item
     * @return Copy of {@link RecipeBuilderShapeless}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShapeless addInput(IItemConvertible stack, int meta){
        ItemStack _stack = stack.getDefaultStack();
        _stack.setMetadata(meta);
        return addInput(_stack);
    }

    /**
     * @param itemGroup ItemGroup to add to recipe's item list
     * @return Copy of {@link RecipeBuilderShapeless}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShapeless addInput(String itemGroup){
        return addInput(new RecipeSymbol(itemGroup));
    }

    /**
     * @param stack Item to add to recipe's item list
     * @return Copy of {@link RecipeBuilderShapeless}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShapeless addInput(ItemStack stack){
        return addInput(new RecipeSymbol(stack));
    }

    /**
     * @param symbol {@link RecipeSymbol} to add to recipe's item list
     * @return Copy of {@link RecipeBuilderShapeless}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShapeless addInput(RecipeSymbol symbol){
        RecipeBuilderShapeless builder = this.clone(this);
        symbolShapelessList.add(symbol);
        return builder;
    }
    @Override
    @SuppressWarnings({"unused", "unchecked"})
    public void create(String recipeID, ItemStack outputStack) {
        ((RecipeGroup<RecipeEntryCrafting<?, ?>>) RecipeBuilder.getRecipeGroup(modID, "workbench", new RecipeSymbol(Block.workbench.getDefaultStack())))
                .register(recipeID, new RecipeEntryCraftingShapeless(symbolShapelessList, outputStack));
    }
}
