package turniplabs.halplibe.helper.network;

import net.minecraft.core.entity.player.Player;

import javax.annotation.Nonnull;

public interface NetworkMessage {
	/**
	 * Encode the UniversalPacket into your NetworkMessage.
	 * This may be called on any thread, so this should be a pure operation.
	 *
	 * @param packet The packet to write data to.
	 */
	void encodeToUniversalPacket(@Nonnull UniversalPacket packet );

	/**
	 * Decode the UniversalPacket into your NetworkMessage.
	 * This may be called on any thread, so this should be a pure operation.
	 *
	 * @param packet The packet to read data from.
	 */
	void decodeFromUniversalPacket(@Nonnull UniversalPacket packet );

	/**
	 * Handle this {@link NetworkMessage}.
	 *
	 * @param context An intermediary representation of Packet handler common on both Client and Server environment.
	 */
	void handle(NetworkContext context);

	class NetworkContext {
		/**
		 * The player that send the NetworkPacket to the handle
		 */
		public Player player;

		public NetworkContext(Player player) {
			this.player = player;
		}
	}

}
