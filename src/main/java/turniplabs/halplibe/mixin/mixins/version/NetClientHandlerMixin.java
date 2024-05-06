package turniplabs.halplibe.mixin.mixins.version;

import net.minecraft.client.net.handler.NetClientHandler;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet250CustomPayload;
import net.minecraft.core.net.packet.Packet2Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.ModVersionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Mixin(value = NetClientHandler.class, remap = false)
public abstract class NetClientHandlerMixin extends NetHandler {

    @Inject(method = "handleHandshake(Lnet/minecraft/core/net/packet/Packet2Handshake;)V", at = @At("HEAD"))
    private void resetServerModlist(Packet2Handshake packet2handshake, CallbackInfo ci){
        try {
            Field m = ModVersionHelper.class.getDeclaredField("serverMods");
            m.setAccessible(true);
            m.set(null, null);
            m.setAccessible(false);
        } catch (Throwable err) {
            err.printStackTrace();
        }
        HalpLibe.LOGGER.info(String.valueOf(ModVersionHelper.getServerModlist() == null));
    }
    @Inject(method = "handleCustomPayload(Lnet/minecraft/core/net/packet/Packet250CustomPayload;)V", at = @At("HEAD"), cancellable = true)
    private void halplibe$handleModlistPacket(Packet250CustomPayload packet, CallbackInfo ci){
        if (packet.channel.equals("halplibe|modlist")){
            try {
                Method m = ModVersionHelper.class.getDeclaredMethod("setServerModlist", List.class);
                m.setAccessible(true);
                m.invoke(null, ModVersionHelper.decodeMods(packet.data));
                m.setAccessible(false);
            } catch (Throwable err) {
                err.printStackTrace();
            }
            ci.cancel();
        }
    }
}
