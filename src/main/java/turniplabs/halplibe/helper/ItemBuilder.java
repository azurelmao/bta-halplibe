package turniplabs.halplibe.helper;

import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.Item;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.HalpLibe;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ItemBuilder implements Cloneable {
    private final String modId;
    @Nullable
    private String overrideKey = null;
    @Nullable
    private Tag<Item>[] tags = null;
    private int stackSize = 64;
    private int maxDamage = 0;
    @Nullable
    private Supplier<Item> containerItemSupplier = null;
    @NotNull
    private Function<Item, ItemModel> customItemModelSupplier;
    public ItemBuilder(String modId){
        this.modId = modId;
        customItemModelSupplier = (item -> new ItemModelStandard(item, modId));
    }
    @Override
    public ItemBuilder clone() {
        try {
            // none of the fields are mutated so this should be fine
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    public ItemBuilder setItemModel(Function<Item, ItemModel> modelSupplier){
        ItemBuilder builder = this.clone();
        builder.customItemModelSupplier = modelSupplier;
        return builder;
    }
    public ItemBuilder setKey(String key){
        ItemBuilder builder = this.clone();
        builder.overrideKey = key;
        return builder;
    }
    public ItemBuilder setStackSize(int stackSize){
        ItemBuilder builder = this.clone();
        builder.stackSize = stackSize;
        return builder;
    }
    public ItemBuilder setMaxDamage(int maxDamage){
        ItemBuilder builder = this.clone();
        builder.maxDamage = maxDamage;
        return builder;
    }
    public ItemBuilder setContainerItem(Supplier<Item> itemSupplier){
        ItemBuilder builder = this.clone();
        builder.containerItemSupplier = itemSupplier;
        return builder;
    }
    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final ItemBuilder setTags(Tag<Item>... tags) {
        ItemBuilder itemBuilder = this.clone();
        itemBuilder.tags = tags;
        return itemBuilder;
    }

    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final ItemBuilder addTags(Tag<Item>... tags) {
        ItemBuilder itemBuilder = this.clone();
        itemBuilder.tags = ArrayUtils.addAll(this.tags, tags);
        return itemBuilder;
    }

    public <T extends Item> T build(T item){
        List<String> tokens;

        if (overrideKey != null){
            tokens = Arrays.stream(overrideKey.split("\\.")).collect(Collectors.toList());
        } else {
            tokens = Arrays.stream(item.getKey().split("\\.")).collect(Collectors.toList());
        }

        if (tags != null) {
            item.withTags(tags);
        }
        if (containerItemSupplier != null){
            item.setContainerItem(containerItemSupplier.get());
        }
        item.setMaxStackSize(stackSize);
        try {
            item.getClass().getMethod("setMaxDamage", int.class).invoke(item, maxDamage);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        List<String> newTokens = new ArrayList<>();
        newTokens.add(modId);
        newTokens.addAll(tokens.subList(1, tokens.size()));

        ItemBuilder.Assignment.queueItemModel(item, customItemModelSupplier);

        item.setKey(StringUtils.join(newTokens, "."));

        return item;
    }

    public static class Assignment{
        public static boolean itemDispatcherInitialized = false;
        public static final Map<Item, Function<Item, ItemModel>> queuedItemModels = new HashMap<>();
        public static <T extends Item> void queueItemModel(@NotNull T item, @NotNull Function<T, ItemModel> itemModelSupplier){
            if (!HalpLibe.isClient) return;
            if (itemDispatcherInitialized){
                ItemModelDispatcher.getInstance().addDispatch(itemModelSupplier.apply(item));
                return;
            }
            queuedItemModels.put(item, (Function<Item, ItemModel>) itemModelSupplier);
        }
    }
}
