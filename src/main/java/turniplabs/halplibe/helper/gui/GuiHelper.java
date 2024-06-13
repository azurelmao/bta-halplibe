package turniplabs.halplibe.helper.gui;

import net.minecraft.core.data.registry.Registry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.gui.factory.IGuiFactory;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

public final class GuiHelper {

    public static final Registry<RegisteredGui> REGISTRY = new Registry<>();

    public static RegisteredGui registerGui(@NotNull String modId, @NotNull String guiId, @NotNull IGuiFactory factory, boolean serverSide) {
        RegisteredGui gui = new RegisteredGui(modId, guiId, factory, serverSide);
        REGISTRY.register(gui.getNamespace(), gui);
        return gui;
    }

    public static RegisteredGui registerClientGui(@NotNull String modId, @NotNull String guiId, @NotNull IGuiFactory factory) {
        return registerGui(modId, guiId, factory, false);
    }

    public static RegisteredGui registerServerGui(@NotNull String modId, @NotNull String guiId, @NotNull IGuiFactory factory) {
        return registerGui(modId, guiId, factory, true);
    }

    public static RegisteredGui getGui(@NotNull String modId, @NotNull String guiId) {
        return REGISTRY.getItem(modId + ":" + guiId);
    }

    public static RegisteredGui getGui(@NotNull String namespace) {
        return REGISTRY.getItem(namespace);
    }

    @ApiStatus.Internal
    public static void reportVanillaGuiCall(String s) {
        HalpLibe.LOGGER.warn(s + ". This is not supposed to happen.");
        HalpLibe.LOGGER.warn("Stacktrace:");
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            HalpLibe.LOGGER.warn("  " + element.toString());
        }
    }
}
