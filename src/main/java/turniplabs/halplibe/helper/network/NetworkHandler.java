package turniplabs.halplibe.helper.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.NetworkHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NetworkHandler
{
    private static final Map<Integer, BiConsumer<NetworkMessage.NetworkContext, UniversalPacket>> packetReaders = new HashMap<>();
    private static final Map<Class<?>, Integer> packetIds = new HashMap<>();

    private NetworkHandler()
    {
    }

    public static void setup()
    {
       NetworkHelper.register ( UniversalPacket.class, true, true );
    }

    public static void receiveUniversalPacket(NetworkMessage.NetworkContext context, UniversalPacket buffer )
    {
        int type = buffer.readByte();
        packetReaders.get( type )
                .accept( context, buffer );
    }

    /**
     * Register a NetworkMessage, and a thread-unsafe handler for it.
     *
     * @param <T>     The type of the NetworkMessage to send.
     * @param id      The identifier for this message type
     * @param factory The factory for this type of message.
     */
    @SuppressWarnings({"unused"})
    public static <T extends NetworkMessage> void registerNetworkMessage( int id, Supplier<T> factory )
    {
        registerNetworkMessage( id, getType( factory ), buf -> {
            T instance = factory.get();
            instance.fromBytes( buf );
            return instance;
        } );
    }

    /**
     * Register a NetworkMessage, and a thread-unsafe handler for it.
     *
     * @param <T>     The type of the NetworkMessage to send.
     * @param type    The class of the type of message to send.
     * @param id      The identifier for this message type
     * @param decoder The factory for this type of message.
     */
    private static <T extends NetworkMessage> void registerNetworkMessage( int id, Class<T> type, Function<UniversalPacket, T> decoder )
    {
        packetIds.put( type, id );
        packetReaders.put( id, ( context, buf ) -> {
            T result = decoder.apply( buf );
            result.handle(context);
        } );
    }

    @SuppressWarnings( "unchecked" )
    private static <T> Class<T> getType( Supplier<T> supplier )
    {
        return (Class<T>) supplier.get()
                .getClass();
    }

    private static UniversalPacket encode(NetworkMessage message )
    {
        UniversalPacket buf = new UniversalPacket();
        buf.writeByte( packetIds.get( message.getClass() ) );
        message.toBytes( buf );
        return buf;
    }

    @Environment(EnvType.CLIENT)
    private static void sendToPlayerLocal(NetworkMessage message)
    {
        message.handle(new NetworkMessage.NetworkContext(Minecraft.getMinecraft().thePlayer));
    }

    @Environment(EnvType.SERVER)
    private static void sendToPlayerServer(Player player, NetworkMessage message )
    {
        ((PlayerServer)player).playerNetServerHandler.sendPacket(encode(message));
    }

    /**
     * Send a NetworkMessage to a specific Player from the server
     * If we are in SinglePlayer this will skip encoding and directly call the message handle
     */
    @SuppressWarnings({"unused"})
    public static void sendToPlayer(Player player, NetworkMessage message )
    {
        if (!EnvironmentHelper.isServerEnvironment()){
            sendToPlayerLocal(message);
            return;
        }
        sendToPlayerServer(player, message);
    }

    /**
     * Send a NetworkMessage to all Players from the server
     * If we are in SinglePlayer this will skip encoding and directly call the message handle
     */
    @SuppressWarnings({"unused"})
    public static void sendToAllPlayers( NetworkMessage packet )
    {
        if (!EnvironmentHelper.isServerEnvironment()){
            sendToPlayerLocal(packet);
            return;
        }
        MinecraftServer.getInstance().playerList.sendPacketToAllPlayers(encode(packet));
    }

    /**
     * Send a NetworkMessage to the Server from the player
     * If we are in SinglePlayer this will skip encoding and directly call the message handle
     */
    @SuppressWarnings({"unused"})
    @Environment( EnvType.CLIENT )
    public static void sendToServer( NetworkMessage packet )
    {
        if (EnvironmentHelper.isSinglePlayer()){
            sendToPlayerLocal(packet);
            return;
        }
        Minecraft.getMinecraft().getSendQueue().addToSendQueue(encode(packet));
    }

    /**
     * Send a NetworkMessage to all Players around a block from the server
     * If we are in SinglePlayer this will skip encoding and directly call the message handle
     */
    @SuppressWarnings({"unused"})
    public static void sendToAllAround(NetworkMessage packet, double x, double y, double z, double radius, int dimension )
    {
        if (!EnvironmentHelper.isServerEnvironment()){
            sendToPlayerLocal(packet);
            return;
        }
        MinecraftServer.getInstance().playerList.sendPacketToPlayersAroundPoint(x, y, z, radius, dimension, encode(packet));
    }
}