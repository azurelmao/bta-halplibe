package turniplabs.halplibe.helper.gui.test;

import net.minecraft.client.gui.GuiContainer;
import net.minecraft.core.entity.player.EntityPlayer;

public class TestGui extends GuiContainer {

    public TestGui(EntityPlayer player, TestTile tile) {
        super(new TestContainer(player, tile));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {

    }
}
