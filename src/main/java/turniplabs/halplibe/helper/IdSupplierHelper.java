package turniplabs.halplibe.helper;

import turniplabs.halplibe.util.toml.Toml;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class IdSupplierHelper {
	private static final ArrayList<Runnable> registryFunctions = new ArrayList<>();
	private static final ArrayList<Runnable> configuredRegistryFunctions = new ArrayList<>();
	private static final ArrayList<Runnable> smartRegistryFunctions = new ArrayList<>();

	/**
	 * Only intended for internal use from {@link BlockBuilder.Registry#reserveRuns(String, Toml, int, Consumer)}
	 *
	 *
	 * @param function the function to run on registry handling
	 */
	public static void scheduleSmartRegistry(Runnable function) {
		smartRegistryFunctions.add(function);
	}

	/**
	 * For blocks, use {@link BlockBuilder.Registry#reserveRuns(String, Toml, int, Consumer)}
	 * These will figure out what ids are available automatically, making sure to account for mods that aren't using halplibe, or are using {@link IdSupplierHelper#scheduleRegistry(boolean, Runnable)}
	 *
	 * Reason this is not deprecated:
	 * - other registries that halplibe doesn't already have utils for
	 * - mods that already have their item count fully defined from the start (i.e. some mod author already knows they will only ever have 2 items)
	 *
	 * @param configured if the mod has already been configured in the past
	 * @param function the function to run upon registering stuff
	 */
	public static void scheduleRegistry(boolean configured, Runnable function) {
		if (configured) configuredRegistryFunctions.add(function);
		else registryFunctions.add(function);
	}
	
	@SuppressWarnings("unused")
	private static void runRegistry() {
		for (Runnable registryFunction : configuredRegistryFunctions) registryFunction.run();
		for (Runnable registryFunction : smartRegistryFunctions) registryFunction.run();
		for (Runnable registryFunction : registryFunctions) registryFunction.run();
	}
}
