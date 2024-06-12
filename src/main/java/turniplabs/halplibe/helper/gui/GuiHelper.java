package turniplabs.halplibe.helper.gui;

import net.minecraft.core.data.registry.Registry;
import net.minecraft.core.entity.player.EntityPlayer;
import turniplabs.halplibe.helper.gui.factory.GuiFactory;

public final class GuiHelper {

    private static int INVENTORY_ID = 0;
    public static final Registry<RegisteredGui> REGISTRY = new Registry<>();

    public static RegisteredGui registerGui(String modId, GuiFactory factory) {
        RegisteredGui gui = new RegisteredGui(modId, INVENTORY_ID, factory);
        REGISTRY.register(String.valueOf(INVENTORY_ID++), gui);
        return gui;
    }

    public static void openGui(int inventoryType, EntityPlayer player, int x, int y, int z) {
        getGui(inventoryType).open(player, x, y, z);
    }

    public static RegisteredGui getGui(int inventoryType) {
        return REGISTRY.getItem(String.valueOf(inventoryType));
    }

}
