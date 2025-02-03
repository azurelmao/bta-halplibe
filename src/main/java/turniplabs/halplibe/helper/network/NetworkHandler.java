package turniplabs.halplibe.helper.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NetworkHandler
{
	private static final List<Supplier<NetworkMessage>> messagesToRegisterForServer = new LinkedList<>(Collections.singletonList(
		MessageIdsNetworkMessage::new
	));

	private static final Map<Short, BiConsumer<NetworkMessage.NetworkContext, UniversalPacket>> packetReaders = new HashMap<>();
	private static final Map<Class<?>, Short> packetIds = new HashMap<>();

	private NetworkHandler()
	{
	}

	public static void setup()
	{
		Packet.addMapping (88,  true, true, UniversalPacket.class );

		register();
	}

	public static void register()
	{
		packetReaders.clear();
		packetIds.clear();

		for (Supplier<NetworkMessage> networkMessage : messagesToRegisterForServer) {
			addNetworkMessage(networkMessage);
		}
	}

	public static void receiveUniversalPacket(NetworkMessage.NetworkContext context, UniversalPacket buffer )
	{
		short type = buffer.readShort();

		if (!packetReaders.containsKey(type)) {
			return;
		}

		packetReaders.get( type )
			.accept( context, buffer );
	}

	/**
	 * Register a NetworkMessage, and a thread-unsafe handler for it.
	 *
	 * @param factory The factory for this type of message.
	 */
	@SuppressWarnings({"unused"})
	public static void registerNetworkMessage( Supplier<NetworkMessage> factory )
	{
		messagesToRegisterForServer.add(factory);
	}

	/**
	 * Register a NetworkMessage, and a thread-unsafe handler for it.
	 *
	 * @param <T>     The type of the NetworkMessage to send.
	 * @param factory The factory for this type of message.
	 */
	@SuppressWarnings({"unused"})
	public static <T extends NetworkMessage> void addNetworkMessage( Supplier<T> factory )
	{
		registerNetworkMessage((short) packetIds.size(), factory);
	}

	/**
	 * Register a NetworkMessage, and a thread-unsafe handler for it.
	 *
	 * @param <T>     The type of the NetworkMessage to send.
	 * @param id      The identifier for this message type
	 * @param factory The factory for this type of message.
	 */
	@SuppressWarnings({"unused"})
	private static <T extends NetworkMessage> void registerNetworkMessage( short id, Supplier<T> factory )
	{
		registerNetworkMessage( id, getType( factory ), buf -> {
			T instance = factory.get();
			instance.decodeFromUniversalPacket( buf );
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
	private static <T extends NetworkMessage> void registerNetworkMessage( short id, Class<T> type, Function<UniversalPacket, T> decoder )
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
		buf.writeShort( packetIds.get( message.getClass() ) );
		message.encodeToUniversalPacket( buf );
		return buf;
	}

	@Environment(EnvType.CLIENT)
	private static void sendToPlayerLocal(NetworkMessage message)
	{
		message.handle(new NetworkMessage.NetworkContext(Minecraft.getMinecraft().thePlayer));
	}

	@Environment(EnvType.SERVER)
	private static void sendToPlayerServer(Player player, NetworkMessage message)
	{
		((PlayerServer)player).playerNetServerHandler.sendPacket(encode(message));
	}

	@Environment(EnvType.SERVER)
	public static void sendToPlayerMessagesConfiguration(Player player)
	{
		((PlayerServer)player).playerNetServerHandler.sendPacket(encode(new MessageIdsNetworkMessage(packetIds)));
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
	public static void sendToAllPlayers( NetworkMessage message )
	{
		if (!EnvironmentHelper.isServerEnvironment()){
			sendToPlayerLocal(message);
			return;
		}
		MinecraftServer.getInstance().playerList.sendPacketToAllPlayers(encode(message));
	}

	/**
	 * Send a NetworkMessage to the Server from the player
	 * If we are in SinglePlayer this will skip encoding and directly call the message handle
	 */
	@SuppressWarnings({"unused"})
	@Environment( EnvType.CLIENT )
	public static void sendToServer( NetworkMessage message )
	{
		if (EnvironmentHelper.isSinglePlayer()){
			sendToPlayerLocal(message);
			return;
		}
		Minecraft.getMinecraft().getSendQueue().addToSendQueue(encode(message));
	}

	/**
	 * Send a NetworkMessage to all Players around a block from the server
	 * If we are in SinglePlayer this will skip encoding and directly call the message handle
	 */
	@SuppressWarnings({"unused"})
	public static void sendToAllAround(double x, double y, double z, double radius, int dimension, NetworkMessage message )
	{
		if (!EnvironmentHelper.isServerEnvironment()){
			sendToPlayerLocal(message);
			return;
		}
		MinecraftServer.getInstance().playerList.sendPacketToPlayersAroundPoint(x, y, z, radius, dimension, encode(message));
	}

	private static class MessageIdsNetworkMessage implements NetworkMessage{
			Map<Class<?>, Short> packetIds;

			public MessageIdsNetworkMessage() {}

			public MessageIdsNetworkMessage(Map<Class<?>, Short> packetIds) {
				this.packetIds = packetIds;
			}

			@Override
			public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
				packet.writeShort((short) packetIds.size());

				for (Map.Entry<Class<?>, Short> entry : packetIds.entrySet()) {
					packet.writeShort(entry.getValue());
					packet.writeString(entry.getKey().getName());
				}
			}

			@Override
			public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
				this.packetIds = new HashMap<>();

				final short size = packet.readShort();

				try {
					for (int i = 0; i < size; i++) {
						final short id = packet.readShort();
						final Class<?> messageClass = Class.forName(packet.readString());

						this.packetIds.put(messageClass, id);
					}
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}

		@Override
		public void handle(NetworkContext context) {
			try {
				NetworkHandler.packetReaders.clear();
				NetworkHandler.packetIds.clear();

				for (Map.Entry<Class<?>, Short> entry : packetIds.entrySet()) {
					Class<?> klass = entry.getKey();
					if (NetworkMessage.class.isAssignableFrom(klass)) {
						Supplier<NetworkMessage> supplier = () -> {
							try {
								return (NetworkMessage) klass.getDeclaredConstructor().newInstance();
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
								throw new RuntimeException(e);
							}
						};
						NetworkHandler.registerNetworkMessage(entry.getValue(), supplier);
					} else {
						throw new IllegalArgumentException("Class " + klass.getName() + " does not extend NetworkMessage");
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
