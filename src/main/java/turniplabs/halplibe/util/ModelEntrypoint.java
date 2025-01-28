package turniplabs.halplibe.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ModelEntrypoint {
    void initBlockModels();
    void initItemModels();
    void initEntityModels();
    void initTileEntityModels();
    void initBlockColors();
}
