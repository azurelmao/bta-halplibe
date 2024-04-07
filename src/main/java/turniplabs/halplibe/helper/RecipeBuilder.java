package turniplabs.halplibe.helper;

import com.b100.utils.FileUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.Global;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.*;
import net.minecraft.core.data.registry.recipe.adapter.*;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.recipeBuilders.*;
import turniplabs.halplibe.helper.recipeBuilders.modifiers.BlastFurnaceModifier;
import turniplabs.halplibe.helper.recipeBuilders.modifiers.FurnaceModifier;
import turniplabs.halplibe.helper.recipeBuilders.modifiers.TrommelModifier;
import turniplabs.halplibe.helper.recipeBuilders.modifiers.WorkbenchModifier;

import javax.annotation.Nonnull;
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
    public static void initNameSpace(String modID){
        getRecipeNamespace(modID);
        RecipeBuilder.getRecipeGroup(modID, "blast_furnace", new RecipeSymbol(Block.furnaceBlastActive.getDefaultStack()));
        RecipeBuilder.getRecipeGroup(modID, "furnace", new RecipeSymbol(Block.furnaceStoneActive.getDefaultStack()));
        RecipeBuilder.getRecipeGroup(modID, "workbench", new RecipeSymbol(Block.workbench.getDefaultStack()));
        RecipeBuilder.getRecipeGroup(modID, "trommel", new RecipeSymbol(Block.trommelActive.getDefaultStack()));
    }
    @Nonnull
    public static RecipeNamespace getRecipeNamespace(String modID){
        if (Registries.RECIPES.getItem(modID) != null){
            return Registries.RECIPES.getItem(modID);
        }
        RecipeNamespace modSpace = new RecipeNamespace();
        Registries.RECIPES.register(modID, modSpace);
        return Objects.requireNonNull(modSpace);
    }
    @Nonnull
    public static RecipeGroup<?> getRecipeGroup(String modID, String key, RecipeSymbol symbol){
        return getRecipeGroup(getRecipeNamespace(modID), key, symbol);
    }
    @Nonnull
    public static RecipeGroup<?> getRecipeGroup(RecipeNamespace namespace, String key, RecipeSymbol symbol){
        if (namespace.getItem(key) != null){
            return namespace.getItem(key);
        }
        RecipeGroup<?> group = new RecipeGroup<>(symbol);
        namespace.register(key, group);
        return Objects.requireNonNull(group);
    }
    @SuppressWarnings("unused")
    public static RecipeBuilderShaped Shaped(String modID){
        return new RecipeBuilderShaped(modID);
    }
    @SuppressWarnings("unused")
    public static RecipeBuilderShaped Shaped(String modID, String... shape){
        return new RecipeBuilderShaped(modID, shape);
    }
    @SuppressWarnings("unused")
    public static RecipeBuilderShapeless Shapeless(String modID){
        return new RecipeBuilderShapeless(modID);
    }
    @SuppressWarnings("unused")
    public static RecipeBuilderFurnace Furnace(String modID){
        return new RecipeBuilderFurnace(modID);
    }
    @SuppressWarnings("unused")
    public static RecipeBuilderBlastFurnace BlastFurnace(String modID){
        return new RecipeBuilderBlastFurnace(modID);
    }
    @SuppressWarnings("unused")
    public static RecipeBuilderTrommel Trommel(String modID){
        return new RecipeBuilderTrommel(modID);
    }
    @SuppressWarnings("unused")
    public static TrommelModifier ModifyTrommel(String namespace, String key){
        return new TrommelModifier(namespace, key);
    }
    @SuppressWarnings("unused")
    public static WorkbenchModifier ModifyWorkbench(String namespace){
        return new WorkbenchModifier(namespace);
    }
    @SuppressWarnings("unused")
    public static FurnaceModifier ModifyFurnace(String namespace){
        return new FurnaceModifier(namespace);
    }
    @SuppressWarnings("unused")
    public static BlastFurnaceModifier ModifyBlastFurnace(String namespace){
        return new BlastFurnaceModifier(namespace);
    }
    public static boolean isExporting = false;
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
