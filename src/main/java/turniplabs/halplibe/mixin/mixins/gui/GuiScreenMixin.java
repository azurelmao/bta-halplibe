package turniplabs.halplibe.mixin.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.util.helper.Listener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.gui.packet.PacketGuiButtonClick;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;
import turniplabs.halplibe.mixin.accessors.gui.GuiScreenAccessor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(
        value = GuiScreen.class,
        remap = false
)
public class GuiScreenMixin implements GuiScreenAccessor {

    @Unique
    private String guiNamespace;

    @Shadow
    public List<GuiButton> controlList;


    @Inject(
            method = "init",
            at = @At("RETURN")
    )
    public void injectInit(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft(GuiScreenMixin.class);
        RegisteredGui gui = getRegisteredGui();
        if(gui == null) return;

        List<Integer> ids = new ArrayList<>(controlList.size());
        for (GuiButton button : controlList) {
            if (ids.contains(button.id))
                throw new IllegalStateException(String.format("Found button in gui '%s' with duplicate id: '%s'.", gui, button.id));

            final Listener<GuiButton> oldListener = button.listener;
            button.setListener(aButton -> {
                if (mc.thePlayer instanceof EntityClientPlayerMP) {
                    EntityClientPlayerMP playerMP = (EntityClientPlayerMP) mc.thePlayer;
                    playerMP.sendQueue.addToSendQueue(new PacketGuiButtonClick(getGuiNamespace(), aButton.id));
                }

                gui.getFactory().onButtonPress(gui, mc.thePlayer, aButton.id);
                if(oldListener != null) oldListener.listen(aButton);
            });

            ids.add(button.id);
        }
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
