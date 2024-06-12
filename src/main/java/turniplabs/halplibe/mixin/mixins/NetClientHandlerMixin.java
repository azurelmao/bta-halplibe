package turniplabs.halplibe.mixin.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.net.handler.NetClientHandler;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet100OpenWindow;
import net.minecraft.core.net.packet.Packet1Login;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.RegisteredGui;
import turniplabs.halplibe.util.RecipeEntrypoint;

@Mixin(value = NetClientHandler.class,remap = false)
public abstract class NetClientHandlerMixin extends NetHandler {

    @Shadow @Final private Minecraft mc;

    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/crafting/legacy/CraftingManager;reset()V", shift = At.Shift.BEFORE))
    public void handleLogin(Packet1Login packet1login, CallbackInfo ci) {
        FabricLoader.getInstance().getEntrypoints("recipesReady", RecipeEntrypoint.class).forEach(RecipeEntrypoint::initNamespaces);
    }

    @Inject(
            method = "handleOpenWindow",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void handleOpenWindowInject(Packet100OpenWindow packet, CallbackInfo ci) {
        RegisteredGui gui = GuiHelper.getGui(packet.inventoryType);
        if(gui != null) {
            gui.openPacket(packet, mc);
            ci.cancel();
        }
    }

}
