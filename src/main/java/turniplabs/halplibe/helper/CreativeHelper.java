package turniplabs.halplibe.helper;

import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.util.CreativeEntry;

public final class CreativeHelper {
    /**
     * @param itemToAdd The itemstack to be added to the creative inventory list
     * @param priority the priority for the item to be added, lower numbers appear higher in the list. Default is 1000
     */
    @SuppressWarnings("unused")
    public static void setPriority(IItemConvertible itemToAdd, int priority){
        setPriority(itemToAdd.getDefaultStack(), priority);
    }

    /**
     * @param itemToAdd The itemstack to be added to the creative inventory list
     * @param meta the meta value for the itemToAdd
     * @param priority the priority for the item to be added, lower numbers appear higher in the list. Default is 1000
     */
    @SuppressWarnings("unused")
    public static void setPriority(IItemConvertible itemToAdd, int meta, int priority){
        setPriority(new ItemStack(itemToAdd, 1, meta), priority);
    }
    /**
     * @param itemToAdd The itemstack to be added to the creative inventory list
     * @param priority the priority for the item to be added, lower numbers appear higher in the list. Default is 1000
     */
    @SuppressWarnings("unused")
    public static void setPriority(ItemStack itemToAdd, int priority){
        CreativeEntry.addEntry(new CreativeEntry(itemToAdd, priority));
    }

    /**
     * @param itemToAdd The itemstack to be added to the creative inventory list
     * @param itemParent The itemstack that the itemToAdd will be placed after
     */
    @SuppressWarnings("unused")
    public static void setParent(IItemConvertible itemToAdd, IItemConvertible itemParent){
        setParent(itemToAdd.getDefaultStack(), itemParent.getDefaultStack());
    }
    /**
     * @param itemToAdd The itemstack to be added to the creative inventory list
     * @param metaToAdd the meta value for the itemToAdd
     * @param itemParent The itemstack that the itemToAdd will be placed after
     * @param metaParent the meta value for the itemParent
     */
    @SuppressWarnings("unused")
    public static void setParent(IItemConvertible itemToAdd, int metaToAdd, IItemConvertible itemParent, int metaParent){
        setParent(new ItemStack(itemToAdd, 1, metaToAdd), new ItemStack(itemParent, metaParent));
    }
    /**
     * @param itemToAdd The itemstack to be added to the creative inventory list
     * @param itemParent The itemstack that the itemToAdd will be placed after
     */
    @SuppressWarnings("unused")
    public static void setParent(ItemStack itemToAdd, ItemStack itemParent){
        CreativeEntry.addEntry(new CreativeEntry(itemToAdd, itemParent));
    }
}
