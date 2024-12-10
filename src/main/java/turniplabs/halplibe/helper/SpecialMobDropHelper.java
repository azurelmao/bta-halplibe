package turniplabs.halplibe.helper;

import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;

public class SpecialMobDropHelper {

    /**
     * Manipulates the loot that can drop when a creeper is killed by a skeleton.
     */
    public static class Creeper {
        private static final WeightedRandomBag<WeightedRandomLootObject> creeperDrops = new WeightedRandomBag<>();

        public static void addDrop(WeightedRandomLootObject drop, int weight) {
            creeperDrops.addEntry(drop,weight);
        }

        public static WeightedRandomBag<WeightedRandomLootObject> getDrops() {
            return creeperDrops;
        }
    }

}
