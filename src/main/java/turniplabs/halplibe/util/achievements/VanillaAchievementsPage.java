package turniplabs.halplibe.util.achievements;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.core.achievement.AchievementList;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class VanillaAchievementsPage extends AchievementPage{
    private static final Block[] stoneOres = new Block[]{Block.oreCoalStone, Block.oreIronStone, Block.oreGoldStone, Block.oreDiamondStone, Block.oreRedstoneStone};
    private static final Block[] basaltOres = new Block[]{Block.oreCoalBasalt, Block.oreIronBasalt, Block.oreGoldBasalt, Block.oreDiamondBasalt, Block.oreRedstoneBasalt};
    public VanillaAchievementsPage() {
        super("Minecraft", "achievements.page.minecraft");
        achievementList.addAll(AchievementList.achievementList);
    }

    @Override
    public void getBackground(GuiAchievements guiAchievements, Random random, int iOffset, int jOffset, int blockX1, int blockY1, int blockX2, int blockY2) {
        int row = 0;
        while (row * 16 - blockY2 < 155) {
            float brightness = 0.6f - (float)(blockY1 + row) / 25.0f * 0.3f;
            GL11.glColor4f(brightness, brightness, brightness, 1.0f);
            int column = 0;
            while (column * 16 - blockX2 < 224) {
                random.setSeed(1234 + blockX1 + column);
                random.nextInt();
                int randomY = random.nextInt(1 + blockY1 + row) + (blockY1 + row) / 2;
                IconCoordinate texture = this.getTextureFromBlock(Block.sand);
                Block[] oreArray = stoneOres;
                if (randomY >= 28 || blockY1 + row > 24) {
                    oreArray = basaltOres;
                }

                if (randomY > 37 || blockY1 + row == 35) {
                    texture = this.getTextureFromBlock(Block.bedrock);
                } else if (randomY == 22) {
                    if (random.nextInt(2) == 0) {
                        texture = this.getTextureFromBlock(oreArray[3]);
                    } else {
                        texture = this.getTextureFromBlock(oreArray[4]);
                    }
                } else if (randomY == 10) {
                    texture = this.getTextureFromBlock(oreArray[1]);
                } else if (randomY == 8) {
                    texture = this.getTextureFromBlock(oreArray[0]);
                } else if (randomY > 4) {
                    texture = this.getTextureFromBlock(Block.stone);
                    if (randomY >= 28 || blockY1 + row > 24) {
                        texture = this.getTextureFromBlock(Block.basalt);
                    }
                } else if (randomY > 0) {
                    texture = this.getTextureFromBlock(Block.dirt);
                }

                guiAchievements.drawTexturedModalRect(
                        iOffset + column * 16 - blockX2,
                        jOffset + row * 16 - blockY2,
                        texture.iconX,
                        texture.iconY,
                        texture.width,
                        texture.height,
                        (double)(1.0F / (float)texture.parentAtlas.getAtlasWidth()),
                        (double)(1.0F / (float)texture.parentAtlas.getAtlasHeight())
                );
                ++column;
            }
            ++row;
        }
    }
    protected IconCoordinate getTextureFromBlock(Block block) {
        return BlockModelDispatcher.getInstance().getDispatch(block).getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
    }
}
