package turniplabs.halplibe.helper.recipeBuilders;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.mixin.accessors.RecipeSymbolAccessor;

import java.util.Arrays;
import java.util.HashMap;

public class RecipeBuilderShaped extends RecipeBuilderBase{
    protected String[] shape;
    protected int width;
    protected int height;
    protected boolean consumeContainer = false;
    protected final HashMap<Character, RecipeSymbol> symbolShapedMap = new HashMap<>();
    /**
     * Used for creating new shaped workbench recipes.
     * @param modID Namespace to create recipe under
     */
    public RecipeBuilderShaped(String modID){
        super(modID);
    }
    /**
     * Used for creating new shaped workbench recipes.
     * @param modID Namespace to create recipe under
     * @param shape Recipe shape in symbol representation
     */
    public RecipeBuilderShaped(String modID, String... shape) {
        super(modID);
        setShapeLocal(shape);
    }

    /**
     * Sets the shape of the recipe
     * Example code:
     * <pre>{@code
     *     RecipeBuilderShaped("minecraft")
     *       .setShape(
     *          "PPP",
     *          "P P",
     *          "PPP")
     *       .addInput('P', Block.planksOak)
     *       .create("chest_planks_oak", Block.chestPlanksOak.getDefaultStack());
     * }</pre>
     * @param shapeTemplate Recipe shape in symbol representation
     * @return Copy of {@link RecipeBuilderShaped}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShaped setShape(String... shapeTemplate){
        RecipeBuilderShaped builder = this.clone(this);
        builder.setShapeLocal(shapeTemplate);
        return builder;
    }
    protected void setShapeLocal(String... shape){
        if (shape == null){
            throw new IllegalArgumentException("Shape Template cannot be set to null!");
        }
        if (shape.length == 0){
            throw new IllegalArgumentException("Shape Template cannot have a size of 0!");
        }
        if (shape.length > 3){
            throw new IllegalArgumentException("Shape Template height cannot exceed 3!\n" + Arrays.toString(shape));
        }
        if (shape[0].length() > 3){
            throw new IllegalArgumentException("Shape Template width cannot exceed 3!\n" + Arrays.toString(shape));
        }
        this.height = shape.length;
        this.width = shape[0].length();

        // Gets the max width
        for (int y = 0; y < this.height; y++) {
            this.width = Math.max(this.width, shape[y].length());
        }

        // Ensures that the recipe shape is always square
        String[] internalShape = new String[height];
        for (int y = 0; y < internalShape.length; y++) {
            StringBuilder builder = new StringBuilder();
            String row = shape[y];
            for (int x = 0; x < width; x++) {
                if (x >= row.length()){
                    builder.append(" ");
                } else {
                    builder.append(row.charAt(x));
                }
            }
            internalShape[y] = builder.toString();
        }

        this.shape = internalShape;
    }

    /**
     * Specifies whether the recipe should consume container items.
     * <pre>{@code
     *     RecipeBuilderShaped("minecraft")
     *       .setShape(
     *          "BBB",
     *          "SES",
     *          "WWW")
     *       .addInput('B', Item.bucketMilk)
     *       .addInput('S', Item.dustSugar)
     *       .addInput('E', Item.eggChicken)
     *       .addInput('W', Item.wheat)
     *       .setConsumeContainer(false) // Recipe will return empty buckets when crafted
     *       .create("cake", Item.cake.getDefaultStack());
     * }</pre>
     * @param consumeContainer Should consume ContainerItem
     * @return Copy of {@link RecipeBuilderShaped}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShaped setConsumeContainer(boolean consumeContainer){
        RecipeBuilderShaped builder = this.clone(this);
        builder.consumeContainer = consumeContainer;
        return builder;
    }

    /**
     * Assigns an item to an item symbol defined in the shape (see {@link #setShape(String...)})
     * @param templateSymbol Item symbol character
     * @param stack Stack to assign
     * @return Copy of {@link RecipeBuilderShaped}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShaped addInput(char templateSymbol, IItemConvertible stack){
        return addInput(templateSymbol, stack, 0);
    }

    /**
     * Assigns an item to an item symbol defined in the shape (see {@link #setShape(String...)})
     * @param templateSymbol Item symbol character
     * @param stack Stack to assign
     * @return Copy of {@link RecipeBuilderShaped}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShaped addInput(char templateSymbol, IItemConvertible stack, int meta){
        ItemStack _stack = stack.getDefaultStack();
        _stack.setMetadata(meta);
        return addInput(templateSymbol, _stack);
    }

    /**
     * Assigns an item to an item symbol defined in the shape (see {@link #setShape(String...)})
     * @param templateSymbol Item symbol character
     * @param stack Stack to assign
     * @return Copy of {@link RecipeBuilderShaped}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShaped addInput(char templateSymbol, ItemStack stack){
        return addInput(templateSymbol, new RecipeSymbol(stack));
    }

    /**
     * Assigns an itemGroup to an item symbol defined in the shape (see {@link #setShape(String...)})
     * @param templateSymbol Item symbol character
     * @param itemGroup ItemGroup key to assign
     * @return Copy of {@link RecipeBuilderShaped}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShaped addInput(char templateSymbol, String itemGroup) {
        return addInput(templateSymbol, new RecipeSymbol(itemGroup));
    }

    /**
     * Assigns an item to an item symbol defined in the shape (see {@link #setShape(String...)})
     * @param templateSymbol Item symbol character
     * @param symbol {@link RecipeSymbol} to assign
     * @return Copy of {@link RecipeBuilderShaped}
     */
    @SuppressWarnings({"unused"})
    public RecipeBuilderShaped addInput(char templateSymbol, RecipeSymbol symbol){
        if (templateSymbol == ' ') throw new IllegalArgumentException("Cannot assign item to protected symbol ' ' pick a different symbol for your recipe input");
        RecipeBuilderShaped builder = this.clone(this);
        symbolShapedMap.put(templateSymbol, symbol);
        return builder;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public void create(String recipeID, ItemStack outputStack) {
        if (shape == null) throw new RuntimeException("Shaped recipe: " + recipeID + " attempted to build without a assigned shape!!");
        RecipeSymbol[] recipe = new RecipeSymbol[height * width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Character cha = null;
                if (shape[y].length() > x) {
                    cha = shape[y].charAt(x);
                }
                RecipeSymbol tempplate = symbolShapedMap.get(cha);
                if (tempplate == null){
                    recipe[x + y * width] = null;
                } else {
                    if(tempplate.getItemGroup() == null){
                        RecipeSymbol s =  new RecipeSymbol(tempplate.getStack());
                        ((RecipeSymbolAccessor)s).setSymbol(cha == null ? ' ' : cha);
                        recipe[x + y * width] = s;
                    } else {
                        recipe[x + y * width] = new RecipeSymbol(cha == null ? ' ' : cha, tempplate.getStack(), tempplate.getItemGroup());
                    }

                }

            }
        }
        ((RecipeGroup<RecipeEntryCrafting<?, ?>>) RecipeBuilder.getRecipeGroup(modID, "workbench", new RecipeSymbol(Block.workbench.getDefaultStack())))
                .register(recipeID, new RecipeEntryCraftingShaped(width, height, recipe, outputStack, consumeContainer));
    }
}
