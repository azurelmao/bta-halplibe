package turniplabs.halplibe.mixin.accessors.gui;

import turniplabs.halplibe.helper.gui.GuiHelper;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

public interface GuiScreenAccessor {

    void setGuiNamespace(String namespace);

    String getGuiNamespace();

    default void setRegisteredGui(RegisteredGui gui) {
        setGuiNamespace(gui.getNamespace());
    }

    default RegisteredGui getRegisteredGui() {
        return GuiHelper.getGui(getGuiNamespace());
    }

}
