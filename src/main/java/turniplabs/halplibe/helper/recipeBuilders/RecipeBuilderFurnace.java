package turniplabs.halplibe.helper.recipeBuilders;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeBuilder;

import java.util.Objects;

public class RecipeBuilderFurnace extends RecipeBuilderBase{
    protected RecipeSymbol input;
    /**
     * Used for creating new furnace recipes.
     * @param modID Namespace to create recipe under
     */
    public RecipeBuilderFurnace(String modID) {
        super(modID);
    }

    /**
     * Furnace recipes can only have one input
     * @param input Input item
     * @return Copy of {@link RecipeBuilderFurnace}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderFurnace setInput(IItemConvertible input){
        return setInput(input, 0);
    }

    /**
     * Furnace recipes can only have one input
     * @param input Input item
     * @param meta Item's required metadata
     * @return Copy of {@link RecipeBuilderFurnace}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderFurnace setInput(IItemConvertible input, int meta){
        return setInput(new ItemStack(input, 1, meta));
    }

    /**
     * Furnace recipes can only have one input
     * @param input Input {@link ItemStack}
     * @return Copy of {@link RecipeBuilderFurnace}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderFurnace setInput(ItemStack input){
        return setInput(new RecipeSymbol(input));
    }

    /**
     * Furnace recipes can only have one input
     * @param itemGroup Input item group
     * @return Copy of {@link RecipeBuilderFurnace}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderFurnace setInput(String itemGroup){
        return setInput(new RecipeSymbol(itemGroup));
    }

    /**
     * Furnace recipes can only have one input
     * @param input {@link RecipeSymbol} Input symbol
     * @return Copy of {@link RecipeBuilderFurnace}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderFurnace setInput(RecipeSymbol input){
        RecipeBuilderFurnace builder = this.clone(this);
        builder.input = Objects.requireNonNull(input, "Input symbol must not be null!");
        return builder;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void create(String recipeID, ItemStack outputStack) {
        Objects.requireNonNull(input, "Input symbol must not be null!");
        ((RecipeGroup<RecipeEntryFurnace>) RecipeBuilder.getRecipeGroup(modID, "furnace", new RecipeSymbol(Blocks.FURNACE_STONE_ACTIVE.getDefaultStack())))
                .register(recipeID, new RecipeEntryFurnace(input, outputStack));
    }
}
