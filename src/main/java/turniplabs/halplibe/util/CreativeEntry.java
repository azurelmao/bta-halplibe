package turniplabs.halplibe.util;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class CreativeEntry implements Comparable<CreativeEntry> {
    public static final LinkedHashMap<String, CreativeEntry> priorityEntryMap = new LinkedHashMap<>();
    public static final LinkedHashMap<String, CreativeEntry> childEntryMap = new LinkedHashMap<>();
    public static void addEntry(CreativeEntry entry){
        String key = entry.stackToAdd.toString();
        if (entry.parentStack != null){
            childEntryMap.put(key, entry);
            priorityEntryMap.remove(key);
        } else {
            priorityEntryMap.put(key, entry);
            childEntryMap.remove(key);
        }
    }
    public int priority;
    public final ItemStack stackToAdd;
    public ItemStack parentStack;
    public CreativeEntry(ItemStack stack){
        this(stack, 1000);
    }
    public CreativeEntry(ItemStack stack, int priority){
        this.stackToAdd = stack.copy();
        this.stackToAdd.stackSize = 1;
        this.priority = priority;
    }
    public CreativeEntry(ItemStack stackToAdd, ItemStack parentStack){
        this.stackToAdd = stackToAdd.copy();
        this.stackToAdd.stackSize = 1;
        this.parentStack = parentStack.copy();
        this.parentStack.stackSize = 1;
    }

    @Override
    public int compareTo(@NotNull CreativeEntry o) {
        return priority - o.priority;
    }
}
