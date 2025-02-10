package turniplabs.halplibe.helper;

import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.Item;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ItemBuilder implements Cloneable {
    private final String modId;
    @Nullable
    private String overrideKey = null;
    @Nullable
    private String textureKey = null;
    @Nullable
    private Tag<Item>[] tags = null;
    private Integer stackSize = null;
    private Integer maxDamage = null;
    @Nullable
    private Supplier<Item> containerItemSupplier = null;
    public ItemBuilder(String modId){
        this.modId = modId;
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

    /**
     * Sets the key to the built {@link Item}, for example if you set the key "gem.sapphire" the actual key ingame will be "item.<modid>.gem.sapphire"
     * @param key Override translation key for the {@link Item}
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setKey(String key){
        ItemBuilder builder = this.clone();
        builder.overrideKey = key;
        return builder;
    }
    /**
     * Sets stack size for the built {@link Item}, will override any class default stacksizes
     * @param stackSize Stack size of the {@link Item}
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setStackSize(int stackSize){
        ItemBuilder builder = this.clone();
        builder.stackSize = stackSize;
        return builder;
    }

    /**
     * Sets max durability for the built {@link Item}, will override any class default max damage values.
     * Probably only really affects tool classes.
     * @param maxDamage Max durability of the {@link Item}
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setMaxDamage(int maxDamage){
        ItemBuilder builder = this.clone();
        builder.maxDamage = maxDamage;
        return builder;
    }

    /**
     * Sets the container item for the built item. For example {@code Item.bucketMilk} uses the container item {@code Item.bucket}
     * @param itemSupplier Supplies the {@link Item} to set as the container item
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setContainerItem(Supplier<Item> itemSupplier){
        ItemBuilder builder = this.clone();
        builder.containerItemSupplier = itemSupplier;
        return builder;
    }

    /**
     * Overrides all previous tags with the ones provided
     * @return @return Copy of {@link ItemBuilder}
     */
    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final ItemBuilder setTags(Tag<Item>... tags) {
        ItemBuilder itemBuilder = this.clone();
        itemBuilder.tags = tags;
        return itemBuilder;
    }

    /**
     * Adds provided tags to previously specified tags
     * @return @return Copy of {@link ItemBuilder}
     */
    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final ItemBuilder addTags(Tag<Item>... tags) {
        ItemBuilder itemBuilder = this.clone();
        itemBuilder.tags = ArrayUtils.addAll(this.tags, tags);
        return itemBuilder;
    }

    /**
     * Applies the builder configuration to the supplied item.
     * @param item Input item object
     * @return Returns the input item after builder settings are applied to it.
     */
    @SuppressWarnings("unused")
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

        if (stackSize != null){
            item.setMaxStackSize(stackSize);
        }

        if (containerItemSupplier != null){
            item.setContainerItem(containerItemSupplier.get());
        }

        if (maxDamage != null){
            item.setMaxDamage(maxDamage);
        }

        List<String> newTokens = new ArrayList<>();
        newTokens.add(modId);
        newTokens.addAll(tokens.subList(1, tokens.size()));

        item.setKey(StringUtils.join(newTokens, "."));

        return item;
    }

}
