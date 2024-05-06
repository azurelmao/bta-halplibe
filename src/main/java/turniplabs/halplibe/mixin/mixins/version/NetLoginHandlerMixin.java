package turniplabs.halplibe.mixin.mixins.version;

import net.minecraft.core.net.packet.Packet1Login;
import net.minecraft.core.net.packet.Packet250CustomPayload;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.handler.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.ModVersionHelper;

@Mixin(value = NetLoginHandler.class, remap = false)
public class NetLoginHandlerMixin {
    @Inject(method = "doLogin(Lnet/minecraft/core/net/packet/Packet1Login;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/handler/NetServerHandler;sendPacket(Lnet/minecraft/core/net/packet/Packet;)V", ordinal = 6, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void modlistPacket(Packet1Login packet1login, CallbackInfo ci, EntityPlayerMP entityplayermp){
        entityplayermp.playerNetServerHandler.sendPacket(new Packet250CustomPayload("halplibe|modlist", ModVersionHelper.encodeMods(ModVersionHelper.getLocalModlist())));
    }

}
