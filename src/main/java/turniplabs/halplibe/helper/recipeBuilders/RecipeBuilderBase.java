package turniplabs.halplibe.helper.recipeBuilders;

import net.minecraft.core.item.ItemStack;

import java.util.Objects;

public abstract class RecipeBuilderBase implements Cloneable {
    protected String modID;
    public RecipeBuilderBase(String modID){
        this.modID = Objects.requireNonNull(modID, "ModID must not be null!");
    }
    @SuppressWarnings({"unchecked", "unused"})
    public <T> T clone(T object){
        return (T) clone();
    }
    @Override
    public RecipeBuilderBase clone() {
        try {
            // none of the fields are mutated so this should be fine
            return (RecipeBuilderBase) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Creates a new recipe from the provided builder arguments.
     * @param recipeID Recipe identifier to assign to the created recipe
     * @param outputStack Result of crafting the specified recipe
     */
    @SuppressWarnings({"unused"})
    protected abstract void create( String recipeID, ItemStack outputStack);
}
