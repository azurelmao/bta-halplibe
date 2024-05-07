package turniplabs.halplibe.helper;

import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.mixin.mixins.models.ItemModelDispatcherMixin;
import turniplabs.halplibe.util.registry.IdSupplier;
import turniplabs.halplibe.util.registry.RunLengthConfig;
import turniplabs.halplibe.util.registry.RunReserves;
import turniplabs.halplibe.util.toml.Toml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract public class ItemHelper {
	/**
	 * Used in {@link ItemModelDispatcherMixin#addQueuedModels(CallbackInfo)}
	 */
	private static boolean itemDispatcherInitialized = false;
	private static final Map<Item, Function<? extends Item, ItemModel>> queuedItemModels = new HashMap<>();
	public static <T extends Item> void queueItemModel(@NotNull T item, @NotNull Function<T, ItemModel> itemModelSupplier){
		if (!HalpLibe.isClient) return;
		if (itemDispatcherInitialized){
			ItemModelDispatcher.getInstance().addDispatch(itemModelSupplier.apply(item));
			return;
		}
		queuedItemModels.put(item, itemModelSupplier);
	}

	public static <T extends Item> T createItem(@NotNull String modId, @NotNull T item){
		return createItem(modId, item, (i) -> new ItemModelStandard(i, modId));
	}
	public static <T extends Item> T createItem(@NotNull String modId, @NotNull T item, @NotNull Function<Item, ItemModel> itemModelSupplier) {
		List<String> tokens = Arrays.stream(item.getKey().split("\\.")).collect(Collectors.toList());

		List<String> newTokens = new ArrayList<>();
		newTokens.add(modId);
		newTokens.addAll(tokens.subList(1, tokens.size()));

		queueItemModel(item, itemModelSupplier);

		return (T) item.setKey(StringUtils.join(newTokens, "."));
	}
	public static class Registry{
		public static int highestVanilla;

		private static final RunReserves reserves = new RunReserves(
				Registry::findOpenIds,
				Registry::findLength
		);

		/**
		 * Should be called in a runnable scheduled with {@link RegistryHelper#scheduleRegistry(boolean, Runnable)}
		 * @param count the amount of needed blocks for the mod
		 * @return the first available slot to register in
		 */
		public static int findOpenIds(int count) {
			int run = 0;
			// block ids should always match the id of their corresponding item
			// therefor, start registering items one after the max block id
			for (int i = Block.blocksList.length + 1; i < Item.itemsList.length; i++) {
				if (Item.itemsList[i] == null && !reserves.isReserved(i)) {
					if (run >= count)
						return (i - run);
					run++;
				} else {
					run = 0;
				}
			}
			return -1;
		}

		public static int findLength(int id, int terminate) {
			int run = 0;
			for (int i = id; i < Item.itemsList.length; i++) {
				if (Item.itemsList[i] == null && !reserves.isReserved(i)) {
					run++;
					if (run >= terminate) return terminate;
				} else {
					return run;
				}
			}
			return run;
		}

		/**
		 * Allows halplibe to automatically figure out where to insert the runs
		 * @param modId     an identifier for the mod, can be anything, but should be something the user can identify
		 * @param runs      a toml object representing configured registry runs
		 * @param neededIds the number of needed ids
		 *                  if this changes after the mod has been configured (i.e. mod updated and now has more items) it'll find new, valid runs to put those items into
		 * @param function  the function to run for registering items
		 */
		public static void reserveRuns(String modId, Toml runs, int neededIds, Consumer<IdSupplier> function) {
			RunLengthConfig cfg = new RunLengthConfig(runs, neededIds);
			cfg.register(reserves);
			RegistryHelper.scheduleSmartRegistry(
					() -> {
						IdSupplier supplier = new IdSupplier(modId, reserves, cfg, neededIds);
						function.accept(supplier);
						supplier.validate();
					}
			);
		}
	}
}
