package turniplabs.halplibe.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;

public class EnvironmentHelper {
    public static boolean isServerEnvironment() {
        return Global.isServer;
    }

    public static boolean isSinglePlayer() {
        if (Global.isServer) {
            return false;
        }

        return !Minecraft.getMinecraft().isMultiplayerWorld();
    }

    public static boolean isClientWorld() {
        return !isSinglePlayer() && !isServerEnvironment();
    }

}
