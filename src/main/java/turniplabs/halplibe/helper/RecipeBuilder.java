package turniplabs.halplibe.helper;

import com.b100.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.Global;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.adapter.ItemStackJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.RecipeSymbolJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.WeightedRandomBagJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.WeightedRandomLootObjectJsonAdapter;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderBlastFurnace;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderFurnace;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderShaped;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderShapeless;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderTrommel;
import turniplabs.halplibe.helper.recipeBuilders.modifiers.BlastFurnaceModifier;
import turniplabs.halplibe.helper.recipeBuilders.modifiers.FurnaceModifier;
import turniplabs.halplibe.helper.recipeBuilders.modifiers.TrommelModifier;
import turniplabs.halplibe.helper.recipeBuilders.modifiers.WorkbenchModifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class RecipeBuilder {
    /**
     * Initializes vanilla work stations for a specified mod id.
     * @param modID ModID to initialize
     */
    @SuppressWarnings("unused")
    public static void initNameSpace(String modID){
        getRecipeNamespace(modID);
        RecipeBuilder.getRecipeGroup(modID, "blast_furnace", new RecipeSymbol(Blocks.FURNACE_BLAST_ACTIVE.getDefaultStack()));
        RecipeBuilder.getRecipeGroup(modID, "furnace", new RecipeSymbol(Blocks.FURNACE_STONE_ACTIVE.getDefaultStack()));
        RecipeBuilder.getRecipeGroup(modID, "workbench", new RecipeSymbol(Blocks.WORKBENCH.getDefaultStack()));
        RecipeBuilder.getRecipeGroup(modID, "trommel", new RecipeSymbol(Blocks.TROMMEL_ACTIVE.getDefaultStack()));
    }

    /**
     * @param modID modId for namespace (or 'minecraft' for vanilla namespace)
     * @return Returns existing recipeNamespace if it already exists, or creates one and returns it if it does not exist.
     */
    @NotNull
    public static RecipeNamespace getRecipeNamespace(String modID){
        if (Registries.RECIPES.getItem(modID) != null){
            return Registries.RECIPES.getItem(modID);
        }
        RecipeNamespace modSpace = new RecipeNamespace();
        Registries.RECIPES.register(modID, modSpace);
        return Objects.requireNonNull(modSpace);
    }

    /**
     * @param modID modId for namespace (or 'minecraft' for vanilla namespace)
     * @param key recipeGroup key
     * @param symbol {@link RecipeSymbol} which represents the workstation
     * @return {@link RecipeGroup} Returns existing RecipeGroup if it already exists, or creates one and returns it if it does not exist.
     */
    @NotNull
    public static RecipeGroup<?> getRecipeGroup(String modID, String key, RecipeSymbol symbol){
        return getRecipeGroup(getRecipeNamespace(modID), key, symbol);
    }
    /**
     * @param namespace {@link RecipeNamespace} which contains the {@link RecipeGroup}
     * @param key recipeGroup key
     * @param symbol {@link RecipeSymbol} which represents the workstation
     * @return {@link RecipeGroup} Returns existing RecipeGroup if it already exists, or creates one and returns it if it does not exist.
     */
    @NotNull
    public static RecipeGroup<?> getRecipeGroup(RecipeNamespace namespace, String key, RecipeSymbol symbol){
        if (namespace.getItem(key) != null){
            return namespace.getItem(key);
        }
        RecipeGroup<?> group = new RecipeGroup<>(symbol);
        namespace.register(key, group);
        return Objects.requireNonNull(group);
    }

    /**
     * Gets or creates a specified ItemGroup. <br></br>
     *
     * Example code:
     * <pre>{@code
     *     RecipeBuilder.getItemGroup("minecraft", "logs")
     * }</pre>
     *
     * @param modID Mod that owns the ItemGroup (or 'minecraft' for vanilla groups)
     * @param key Group key
     * @return Returns the existing ItemGroup if it exists, or create one and return that if it doesn't yet exist.
     */
    @NotNull
    public static List<ItemStack> getItemGroup(String modID, String key){
        List<ItemStack> group = Registries.ITEM_GROUPS.getItem(String.format("%s:%s", modID, key));
        if (group == null){
            group = new ArrayList<>();
            Registries.ITEM_GROUPS.register(String.format("%s:%s", modID, key), group);
        }
        return group;
    }

    /**
     * Adds specified items to an ItemGroup. If the group does not exist before the method is called then it will be created. <br></br>
     *
     * Example code:
     * <pre>{@code
     *     RecipeBuilder.addItemsToGroup("minecraft", "cobblestones",
     *          Block.cobbleStone,
     *          Block.cobbleBasalt,
     *          Block.cobbleLimestone,
     *          Block.cobbleGranite,
     *          Block.cobblePermafrost)
     * }</pre>
     *
     * @param modID Mod that owns the ItemGroup (or 'minecraft' for vanilla groups)
     * @param key Group key
     * @param items List of only {@link IItemConvertible} (which includes Blocks/Items) and {@link ItemStack}
     */
    @SuppressWarnings("unused")
    public static void addItemsToGroup(String modID, String key, Object ... items ){
        List<ItemStack> group = getItemGroup(modID, key);
        for (Object o : items){
            if (o instanceof IItemConvertible){
                group.add(((IItemConvertible) o).getDefaultStack());
                continue;
            }
            if (o instanceof ItemStack){
                group.add((ItemStack) o);
                continue;
            }
            throw new IllegalArgumentException(String.format("Object '%s' has invalid class '%s'! Only classes that extend 'IItemConvertible' or 'ItemStack' are allowed!", o.toString(), o.getClass().getSimpleName()));
        }
    }

    /**
     * Returns a new {@link RecipeBuilderShaped} <br>
     * Used for creating new shaped workbench recipes.
     */
    @SuppressWarnings("unused")
    public static RecipeBuilderShaped Shaped(String modID){
        return new RecipeBuilderShaped(modID);
    }

    /**
     * Returns a new {@link RecipeBuilderShaped} with its shape set <br>
     * Used for creating new shaped workbench recipes.
     */
    @SuppressWarnings("unused")
    public static RecipeBuilderShaped Shaped(String modID, String... shape){
        return new RecipeBuilderShaped(modID, shape);
    }

    /**
     * Returns a new {@link RecipeBuilderShapeless} <br>
     * Used for creating new shapeless workbench recipes
     */
    @SuppressWarnings("unused")
    public static RecipeBuilderShapeless Shapeless(String modID){
        return new RecipeBuilderShapeless(modID);
    }

    /**
     * Returns a new {@link RecipeBuilderFurnace} <br>
     * Used for creating new furnace recipes.
     */
    @SuppressWarnings("unused")
    public static RecipeBuilderFurnace Furnace(String modID){
        return new RecipeBuilderFurnace(modID);
    }

    /**
     * Returns a new {@link RecipeBuilderBlastFurnace} <br>
     * Used for creating new blast furnace recipes.
     */
    @SuppressWarnings("unused")
    public static RecipeBuilderBlastFurnace BlastFurnace(String modID){
        return new RecipeBuilderBlastFurnace(modID);
    }

    /**
     * Returns a new {@link RecipeBuilderTrommel} <br>
     * Used for creating new trommel recipes.
     */
    @SuppressWarnings("unused")
    public static RecipeBuilderTrommel Trommel(String modID){
        return new RecipeBuilderTrommel(modID);
    }

    /**
     * Returns a new {@link TrommelModifier} <br>
     * Used for modifying existing trommel recipes.
     */
    @SuppressWarnings("unused")
    public static TrommelModifier ModifyTrommel(String namespace, String key){
        return new TrommelModifier(namespace, key);
    }

    /**
     * Returns a new {@link WorkbenchModifier} <br>
     * Used for modifying existing workbench recipes.
     */
    @SuppressWarnings("unused")
    public static WorkbenchModifier ModifyWorkbench(String namespace){
        return new WorkbenchModifier(namespace);
    }

    /**
     * Returns a new {@link FurnaceModifier} <br>
     * Used for modifying existing furnace recipes.
     */
    @SuppressWarnings("unused")
    public static FurnaceModifier ModifyFurnace(String namespace){
        return new FurnaceModifier(namespace);
    }

    /**
     * Returns a new {@link BlastFurnaceModifier} <br>
     * Used for modifying existing blast furnace recipes.
     */
    @SuppressWarnings("unused")
    public static BlastFurnaceModifier ModifyBlastFurnace(String namespace){
        return new BlastFurnaceModifier(namespace);
    }
    public static boolean isExporting = false;

    /**
     * Serializes all loaded recipes to json and dumps the output to ".minecraft-bta/recipeDump/recipes.json"
     */
    @SuppressWarnings("unchecked")
    public static void exportRecipes(){
        isExporting = true;
        Path filePath = Paths.get(Global.accessor.getMinecraftDir() + "/" + "recipeDump");
        createDir(filePath);
        String path = filePath + "/recipes.json";
        List<RecipeEntryBase<?, ?, ?>> recipes = Registries.RECIPES.getAllSerializableRecipes();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        ArrayList<RecipeJsonAdapter<?>> usedAdapters = new ArrayList<>();
        for (RecipeEntryBase<?, ?, ?> recipe : recipes) {
            HasJsonAdapter hasJsonAdapter = (HasJsonAdapter) recipe;
            RecipeJsonAdapter<?> recipeJsonAdapter = hasJsonAdapter.getAdapter();
            if (usedAdapters.contains(recipeJsonAdapter)) continue;
            builder.registerTypeAdapter(recipe.getClass(), recipeJsonAdapter);
            usedAdapters.add(recipeJsonAdapter);
        }
        builder.registerTypeAdapter(ItemStack.class, new ItemStackJsonAdapter());
        builder.registerTypeAdapter(RecipeSymbol.class, new RecipeSymbolJsonAdapter());
        builder.registerTypeAdapter(new TypeToken<WeightedRandomBag<WeightedRandomLootObject>>(){}.getType(), new WeightedRandomBagJsonAdapter());
        builder.registerTypeAdapter(WeightedRandomLootObject.class, new WeightedRandomLootObjectJsonAdapter());
        Gson gson = builder.create();
        JsonArray jsonArray = new JsonArray();
        for (RecipeEntryBase<?, ?, ?> recipeEntryBase : recipes) {
            TypeAdapter<RecipeEntryBase<?, ?, ?>> typeAdapter = (TypeAdapter<RecipeEntryBase<?, ?, ?>>) gson.getAdapter(recipeEntryBase.getClass());
            JsonElement json = typeAdapter.toJsonTree(recipeEntryBase);
            jsonArray.add(json);
        }
        File file = FileUtils.createNewFile(new File(path));
        try (FileWriter fileWriter = new FileWriter(file)){
            gson.toJson(jsonArray, fileWriter);
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        isExporting = false;
    }
    private static void createDir(Path path){
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
    }
}
