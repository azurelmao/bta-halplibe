package turniplabs.halplibe.mixin.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityClientPlayerMP;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import turniplabs.halplibe.helper.gui.GuiScreenAccessor;
import turniplabs.halplibe.helper.gui.packet.PacketGuiButtonClick;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

import java.util.Iterator;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(
        value = GuiScreen.class,
        remap = false
)
public class GuiScreenMixin implements GuiScreenAccessor {

    @Unique
    private String guiNamespace;

    @Inject(
            method = "mouseClicked",
            locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundManager;playSound(Ljava/lang/String;Lnet/minecraft/core/sound/SoundCategory;FF)V",
                    shift = At.Shift.AFTER
            )
    )
    public void injectMouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci, Iterator<GuiButton> unused, GuiButton button) {
        RegisteredGui gui = getRegisteredGui();
        if(gui == null) return;

        EntityPlayerSP player = Minecraft.getMinecraft(GuiScreenMixin.class).thePlayer;

        if (player instanceof EntityClientPlayerMP) {
            EntityClientPlayerMP playerMP = (EntityClientPlayerMP) player;
            playerMP.sendQueue.addToSendQueue(new PacketGuiButtonClick(getGuiNamespace(), button.id));
        }

        gui.getFactory().onButtonClick(gui, player, player, button.id);
    }

    @Override
    public void setGuiNamespace(String namespace) {
        if(guiNamespace != null) throw new IllegalStateException("Gui namespace has already been set!");
        this.guiNamespace = namespace;
    }

    @Override
    public String getGuiNamespace() {
        return guiNamespace;
    }
}
