package turniplabs.halplibe.helper;

import net.minecraft.core.net.packet.Packet;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.HalpLibe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Consumer;

public final class NetworkHelper {
    private static final TreeSet<NetworkEntry> ENTRIES = new TreeSet<>();
    private static boolean locked = false;

    public static void register(Class<? extends Packet> clazz, boolean toServer, boolean toClient) {
        if (locked)
            // if a packet is registered after the game starts, the game will most likely crash due to int[]'s in network managers not being long enough
            throw new RuntimeException("Packet " + clazz + " was registered too late, packets should be registered before the game starts running.");
        ENTRIES.add(new NetworkEntry(clazz, toServer, toClient));
    }

    public static void iterateEntries(Consumer<NetworkEntry> consumer) {
        ENTRIES.forEach(consumer);
    }

    public static int getLastPacketId() {
        return lastPacket;
    }
    public static boolean useExtendedPacketID(){
        return getLastPacketId() > 255;
    }

    // this class is likely more or less redundant, I'm just using it to try to minimize issues from mod load ordering
    public static final class NetworkEntry implements Comparable<NetworkEntry> {
        public final Class<? extends Packet> clazz;

        public final boolean toServer, toClient;

        public NetworkEntry(Class<? extends Packet> clazz, boolean toServer, boolean toClient) {
            this.clazz = clazz;
            this.toServer = toServer;
            this.toClient = toClient;
        }

        @Override
        public int compareTo(@NotNull NetworkEntry o) {
            String mname = clazz.getName();
            String oname = o.clazz.getName();

            int v = Integer.compare(mname.length(), oname.length());

            if (v == 0) {
                for (int i = 0; i < mname.length(); i++) {
                    v = Character.compare(mname.charAt(i), oname.charAt(i));
                    if (v != 0) break;
                }
            }

            return v;
        }
    }

    private static int latestId = 0;
    private static int lastPacket = 0;
    private static final Map<Integer, Class<? extends Packet>> map;
    private static final Method addMapping;
    static {
        try {
            Field f = Packet.class.getDeclaredField("packetIdToClassMap");
            f.setAccessible(true);
            map = (Map<Integer, Class<? extends Packet>>) f.get(null);
            for (Integer integer : map.keySet())
                lastPacket = Math.max(integer, lastPacket);

            f.setAccessible(false);

            addMapping = Packet.class.getDeclaredMethod("addMapping", int.class, boolean.class, boolean.class, Class.class);
        } catch (Throwable err) {
            throw new RuntimeException(err);
        }
    }

    @SuppressWarnings("unused")
    private static void doRegister(Class<? extends Packet> packet, boolean server, boolean client) {
        try {
            while (map.containsKey(latestId)) latestId++;
            if (lastPacket < latestId) lastPacket = latestId;

            addMapping.setAccessible(true);
            addMapping.invoke(null, latestId, client, server, packet);
            addMapping.setAccessible(false);

            locked = true;
            HalpLibe.LOGGER.info("Successfully registered packet " + packet.getName() + " with id " + latestId);
        } catch (Throwable ignored) {
            HalpLibe.LOGGER.warn("Packet Registration failed for packet " + packet.getName());
        }
    }
}
