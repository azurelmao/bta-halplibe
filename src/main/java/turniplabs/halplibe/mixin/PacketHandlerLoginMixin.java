package turniplabs.halplibe.mixin;

import net.minecraft.core.net.packet.PacketLogin;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerLogin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import turniplabs.halplibe.helper.network.NetworkHandler;

@Mixin(value = PacketHandlerLogin.class, remap = false)
public class PacketHandlerLoginMixin {
		@Inject(method = "doLogin(Lnet/minecraft/core/net/packet/PacketLogin;)V", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/server/net/handler/PacketHandlerServer;sendPacket(Lnet/minecraft/core/net/packet/Packet;)V",
			ordinal = 0, shift = At.Shift.AFTER),
			locals = LocalCapture.CAPTURE_FAILHARD
		)
		public void doLogin(PacketLogin packetlogin, CallbackInfo ci, PlayerServer player) {
			NetworkHandler.sendToPlayerMessagesConfiguration(player);
		}
	}
