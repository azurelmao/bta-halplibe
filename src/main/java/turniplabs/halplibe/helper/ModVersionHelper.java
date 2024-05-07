package turniplabs.halplibe.helper;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.Minecraft;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.util.version.EnumModList;
import turniplabs.halplibe.util.version.ModInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ModVersionHelper {
    protected static boolean isDev = FabricLoader.getInstance().isDevelopmentEnvironment();
    protected static List<ModInfo> localMods = new ArrayList<>();
    protected static List<ModInfo> serverMods = null;
    static {
        for (ModContainer modContainer: FabricLoader.getInstance().getAllMods()) {
            if (modContainer.getMetadata().getType().equals("fabric") && !modContainer.getMetadata().getId().equals("fabricloader")){
                localMods.add(new ModInfo(modContainer));
            }
        }
    }
    public static void initialize(){}

    /**
     * @return List of mods installed in the current environment.
     */
    public static List<ModInfo> getLocalModlist(){
        return localMods;
    }

    /**
     * @return List of mods from the current server, returns null if not connected to a server or the server does not support the mod list packet.
     */
    public static List<ModInfo> getServerModlist(){
        if (isClientOfServer()){
            return serverMods;
        }
        return null;
    }
    /**
     * @return Returns local mods when in single player or called from the server, returns server mods when a client of server
     */
    public static List<ModInfo> getActiveModlist(){
        if (isClientOfServer()){
            return getServerModlist();
        }
        return getLocalModlist();
    }

    /**
     * @return Returns true only if the player is currently connected to a server.
     */
    public static boolean isClientOfServer(){
        if (!HalpLibe.isClient){
            return false;
        }
        Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
        return mc.theWorld != null && mc.theWorld.isClientSide;
    }
    /**
     * @return Returns true if the mod is present in the active mod list
     */
    @SuppressWarnings("unused") // API function
    public static boolean isModPresent(String modIdToCheck){
        return isModPresent(modIdToCheck, EnumModList.ACTIVE);
    }
    /**
     * @return Returns true if the mod is present in the specified mod list
     */
    public static boolean isModPresent(String modIdToCheck, EnumModList modList){
        return getModInfo(modIdToCheck, modList) != null;
    }
    /**
     * @return Returns 0 if versions match exactly, returns a positive integer when mod has a greater version number, returns a negative integer when a mod has a smaller version number, returns null if mod is not present or comparison fails.
     */
    @SuppressWarnings("unused") // API function
    public static Integer isVersionPresent(String modIdToCheck, Version version){
        return isVersionPresent(modIdToCheck, version, EnumModList.ACTIVE);
    }
    /**
     * @return Returns 0 if versions match exactly, returns a positive integer when mod has a greater version number, returns a negative integer when a mod has a smaller version number, returns null if mod is not present or comparison fails.
     */
    public static Integer isVersionPresent(String modIdToCheck, Version version, EnumModList modList){
        ModInfo modInfo = getModInfo(modIdToCheck, modList);
        if (modInfo == null) return null;
        return modInfo.version.compareTo(version);
    }
    /**
     * @return Returns the specified mod info if present, returns null if not present
     */
    public static ModInfo getModInfo(String modId, EnumModList modList){
        List<ModInfo> modInfoList = getModList(modList);
        if (modInfoList == null) return null;
        for (ModInfo info: modInfoList) {
            if (info.id.equals(modId)) return info;
        }
        return null;
    }
    /**
     * @return Returns the specified mod list.
     */
    public static List<ModInfo> getModList(EnumModList modList){
        switch (modList){
            case LOCAL:
                return getLocalModlist();
            case SERVER:
                return getServerModlist();
            case ACTIVE:
                return getActiveModlist();
            default:
                return null;
        }
    }
    protected static void setServerModlist(List<ModInfo> modInfos){
        serverMods = modInfos;
        if (isDev){
            printModList();
        }

    }
    protected static void printModList(){
        HalpLibe.LOGGER.info("Server Mod List");
        if (getServerModlist() != null){
            for (ModInfo info: getServerModlist()) {
                HalpLibe.LOGGER.info(info.id + " " + info.version);
            }
        }
    }
    public static byte[] encodeMods(List<ModInfo> modInfo){
        byte[] bytes = new byte[0];
        try {
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteOutput);
            int size = modInfo.size();
            for (int i = 0; i < size; i++) {
                modInfo.get(i).pack(dataOutputStream);
                if (i == size -1){
                    dataOutputStream.writeChar(127);
                } else {
                    dataOutputStream.writeChar(3);
                }
            }
            bytes = byteOutput.toByteArray();
            byteOutput.close();
            dataOutputStream.close();
            return bytes;
        } catch (IOException e) {
            HalpLibe.LOGGER.error("IOException occurred in encoding!", e);
            return bytes;
        }
    }
    public static List<ModInfo> decodeMods(byte[] bytes){
        try {
            List<ModInfo> modInfos = new ArrayList<>();
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));
            char data = 0;
            while (data != 127){
                modInfos.add(new ModInfo(dataInputStream));
                data = dataInputStream.readChar();
            }
            return modInfos;
        } catch (IOException e) {
            HalpLibe.LOGGER.error("IOException occurred in decoding!", e);
            return new ArrayList<>();
        }
    }
}
