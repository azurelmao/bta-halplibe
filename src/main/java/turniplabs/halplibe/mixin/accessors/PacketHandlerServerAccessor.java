package turniplabs.halplibe.mixin.accessors;

import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = PacketHandlerServer.class, remap = false)
public interface PacketHandlerServerAccessor {
    @Accessor
    PlayerServer getPlayerEntity();
}
