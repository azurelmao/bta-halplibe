package turniplabs.halplibe.helper.recipeBuilders;

import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryTrommel;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeBuilder;

import java.util.Objects;

public class RecipeBuilderTrommel extends RecipeBuilderBase{
    protected RecipeSymbol input;
    protected WeightedRandomBag<WeightedRandomLootObject> bag = new WeightedRandomBag<>();
    /**
     * Used for creating new trommel recipes.
     * @param modID Namespace to create recipe under
     */
    public RecipeBuilderTrommel(String modID) {
        super(modID);
    }

    /**
     * Trommel recipes can only have one input
     * @param item Input item
     * @return Copy of {@link RecipeBuilderTrommel}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderTrommel setInput(IItemConvertible item){
        return setInput(item, 0);
    }

    /**
     * Trommel recipes can only have one input
     * @param item Input item
     * @param meta Required metadata of input item
     * @return Copy of {@link RecipeBuilderTrommel}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderTrommel setInput(IItemConvertible item, int meta){
        return setInput(new ItemStack(item, 1, meta));
    }

    /**
     * Trommel recipes can only have one input
     * @param input Input {@link ItemStack}
     * @return Copy of {@link RecipeBuilderTrommel}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderTrommel setInput(ItemStack input){
        return setInput(new RecipeSymbol(input));
    }

    /**
     * Trommel recipes can only have one input
     * @param itemGroup Input itemGroup
     * @return Copy of {@link RecipeBuilderTrommel}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderTrommel setInput(String itemGroup){
        return setInput(new RecipeSymbol(itemGroup));
    }

    /**
     * Trommel recipes can only have one input
     * @param input Input {@link RecipeSymbol}
     * @return Copy of {@link RecipeBuilderTrommel}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderTrommel setInput(RecipeSymbol input){
        RecipeBuilderTrommel builder = this.clone(this);
        builder.input = Objects.requireNonNull(input, "Input symbol must not be null!");
        return builder;
    }

    /**
     * Adds a potential output entry for the recipe
     * <pre>{@code
     *     RecipeBuilderTrommel("minecraft")
     *          .setInput("minecraft:dirt")
     *          .addEntry(new WeightedRandomLootObject(Item.ammoPebble.getDefaultStack(), 1, 3), 60.24)
     *          .addEntry(new WeightedRandomLootObject(Item.clay.getDefaultStack()), 24.1)
     *          .addEntry(new WeightedRandomLootObject(Item.flint.getDefaultStack()), 12.05)
     *          .addEntry(new WeightedRandomLootObject(Item.sulphur.getDefaultStack()), 2.41)
     *          .addEntry(new WeightedRandomLootObject(Item.oreRawIron.getDefaultStack()), 0.6)
     *          .addEntry(new WeightedRandomLootObject(Item.olivine.getDefaultStack()), 0.3)
     *          .addEntry(new WeightedRandomLootObject(Item.quartz.getDefaultStack()), 0.3)
     *          .create("dirt");
     * }</pre>
     * @param lootObject {@link WeightedRandomLootObject} provides possible outs
     * @param weight Comparative probability that this loot object will be selected, higher weights means more likely
     * @return Copy of {@link RecipeBuilderTrommel}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderTrommel addEntry(WeightedRandomLootObject lootObject, double weight){
        RecipeBuilderTrommel builder = this.clone(this);
        builder.bag.addEntry(lootObject, weight);
        return builder;
    }

    /**
     * Creates a new recipe from the provided builder arguments.
     * @param recipeID Recipe identifier to assign to the created recipe
     */
    @SuppressWarnings({"unused", "unchecked"})
    public void create(String recipeID) {
        Objects.requireNonNull(input, "Input symbol must not be null!");
        Objects.requireNonNull(bag, "Weighted Bag must not be null!");
        ((RecipeGroup<RecipeEntryTrommel>) RecipeBuilder.getRecipeGroup(modID, "trommel", new RecipeSymbol(Blocks.TROMMEL_ACTIVE.getDefaultStack())))
                .register(recipeID, new RecipeEntryTrommel(input, bag));
    }
    @Override
    public void create(String recipeID, ItemStack outputStack) throws IllegalArgumentException {
        // Standard create method doesn't apply to this class
        throw new IllegalArgumentException("Use create(String recipeID), create(String recipeID, ItemStack outputStack) does not apply for trommels");
    }
}
