package turniplabs.halplibe.util;

import net.minecraft.core.Global;

public class BlockCoords {
    public static boolean[][] usedCoordinates = new boolean[Global.TEXTURE_ATLAS_WIDTH_TILES][Global.TEXTURE_ATLAS_WIDTH_TILES];

    public static int[] nextCoords() {
        for (int y = 0; y < usedCoordinates[0].length; y++) {
            for (int x = 0; x < usedCoordinates.length; x++) {
                if (usedCoordinates[x][y]) continue;
                markIDUsed(x, y);
                return new int[]{x, y};
            }
        }
        throw new NullPointerException("Out of Texture space! Increase the atlas size in the halplibe config");
    }
    public static void markIDUsed(int x, int y){
        if (x > Global.TEXTURE_ATLAS_WIDTH_TILES || y > Global.TEXTURE_ATLAS_WIDTH_TILES){
            throw new IllegalArgumentException("Coordinates [" + x + ", " + y + "] is outside the valid range of [" + Global.TEXTURE_ATLAS_WIDTH_TILES + ", " + Global.TEXTURE_ATLAS_WIDTH_TILES + "]");
        }
        usedCoordinates[x][y] = true;
    }
    public static void markSectionUsed(int[] topLeft, int[] bottomRight){
        if (topLeft[0] > Global.TEXTURE_ATLAS_WIDTH_TILES || topLeft[1] > Global.TEXTURE_ATLAS_WIDTH_TILES){
            throw new IllegalArgumentException("Coordinates [" + topLeft[0] + ", " + topLeft[1] + "] is outside the valid range of [" + Global.TEXTURE_ATLAS_WIDTH_TILES + ", " + Global.TEXTURE_ATLAS_WIDTH_TILES + "]");
        }
        if (bottomRight[0] > Global.TEXTURE_ATLAS_WIDTH_TILES || bottomRight[1] > Global.TEXTURE_ATLAS_WIDTH_TILES){
            throw new IllegalArgumentException("Coordinates [" + bottomRight[0] + ", " + bottomRight[1] + "] is outside the valid range of [" + Global.TEXTURE_ATLAS_WIDTH_TILES + ", " + Global.TEXTURE_ATLAS_WIDTH_TILES + "]");
        }
        for (int x = topLeft[0]; x <= bottomRight[0]; x++) {
            for (int y = topLeft[1]; y <= bottomRight[1]; y++) {
                markIDUsed(x, y);
            }
        }
    }
    static {
        markSectionUsed(new int[]{0,0}, new int[]{17, 15});
        markSectionUsed(new int[]{18,0}, new int[]{19, 6});
        markSectionUsed(new int[]{20,0}, new int[]{21, 3});
        markSectionUsed(new int[]{0,16}, new int[]{4, 28});
        markSectionUsed(new int[]{0,31}, new int[]{8, 31});
        markSectionUsed(new int[]{20,20}, new int[]{31, 31});
        markSectionUsed(new int[]{16,24}, new int[]{19, 31});
        markSectionUsed(new int[]{5,17}, new int[]{6, 24});
        markSectionUsed(new int[]{7,18}, new int[]{10, 20});
        markIDUsed(5, 16);
        markIDUsed(15, 16);
        markIDUsed(16, 16);
        markIDUsed(18, 8);
        markIDUsed(18, 9);
        markIDUsed(18, 10);
        markIDUsed(22, 0);
        markIDUsed(23, 0);
    }
}
