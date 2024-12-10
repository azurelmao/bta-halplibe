package turniplabs.halplibe.helper;

import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
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
import java.util.List;
import java.util.function.Function;
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
    @NotNull
    private Function<Item, ItemModel> customItemModelSupplier;
    public ItemBuilder(String modId){
        this.modId = modId;
        customItemModelSupplier = (item -> new ItemModelStandard(item, null));
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
     * Sets the ItemModel to be assigned to the {@link Item} built.
     * @param modelSupplier Provides the {@link Item} built in {@link #build(Item)} to a lambda that returns an {@link ItemModel}
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setItemModel(Function<Item, ItemModel> modelSupplier){
        ItemBuilder builder = this.clone();
        builder.customItemModelSupplier = modelSupplier;
        return builder;
    }
    /**
     * Sets the icon for the {@link Item}'s {@link ItemModel}, only works if the ItemModel used extends {@link ItemModelStandard}
     * @param iconKey texture key for the icon for the item to use. Example "minecraft:item/stick"
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings("unused")
    public ItemBuilder setIcon(String iconKey){
        ItemBuilder builder = clone();
        builder.textureKey = iconKey;
        return builder;
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
            try {
                item.getClass().getMethod("setMaxDamage", int.class).invoke(item, maxDamage);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> newTokens = new ArrayList<>();
        newTokens.add(modId);
        newTokens.addAll(tokens.subList(1, tokens.size()));

        Assignment.queueItemModel(item.id, customItemModelSupplier, textureKey);

        item.setKey(StringUtils.join(newTokens, "."));

        return item;
    }

    public static class Assignment{
        public static boolean itemDispatcherInitialized = false;
        public static final List<ItemAssignmentEntry<?>> queuedItemModels = new ArrayList<>();
        /**
         *  Queues a ItemModel assignment until the game is ready to do so
         */
        public static <T extends Item> void queueItemModel(int id, @NotNull Function<T, ItemModel> itemModelSupplier, @Nullable String iconTexture){
            if (!HalpLibe.isClient) return;
            if (itemDispatcherInitialized){
                ItemModelDispatcher.getInstance().addDispatch(new ItemAssignmentEntry<>(id, itemModelSupplier, iconTexture).getModel());
                return;
            }
            queuedItemModels.add(new ItemAssignmentEntry<>(id, itemModelSupplier, iconTexture));
        }
        public static class ItemAssignmentEntry<T extends Item>{
            public final int itemId;
            public final Function<T, ItemModel> modelFunction;
            public final String iconKey;

            public ItemAssignmentEntry(int id, Function<T, ItemModel> modelFunction, String iconKey){
                this.itemId = id;
                this.modelFunction = modelFunction;
                this.iconKey = iconKey;
            }
            public ItemModel getModel(){
                T item = (T) Item.itemsList[itemId];
                ItemModel model = modelFunction.apply(item);

                if (model instanceof ItemModelStandard && iconKey != null){
                    ((ItemModelStandard) model).icon = TextureRegistry.getTexture(iconKey);
                    return model;
                }
                if (model instanceof ItemModelStandard && ((ItemModelStandard) model).icon == ItemModelStandard.ITEM_TEXTURE_UNASSIGNED){
                    String namespace = item.getKey().split("\\.")[1];
                    // Unholy string fuckery
                    ((ItemModelStandard) model).icon = TextureRegistry.getTexture(String.format("%s:item/%s", namespace,
                            item.getKey().replaceFirst("item." + namespace + ".", "").replace(".", "_")));
                    return model;
                }
                return model;
            }
        }
    }
}
