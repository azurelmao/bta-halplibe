package turniplabs.halplibe.helper.network;

import net.minecraft.core.entity.player.Player;

import javax.annotation.Nonnull;

public interface NetworkMessage {
    /**
     * Write this packet to a buffer.
     * This may be called on any thread, so this should be a pure operation.
     *
     * @param packet The packet to write data to.
     */
    void toBytes( @Nonnull UniversalPacket packet );

    /**
     * Read this packet from a buffer.
     * This may be called on any thread, so this should be a pure operation.
     *
     * @param buf The packet to read data from.
     */
    void fromBytes( @Nonnull UniversalPacket buf );

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
