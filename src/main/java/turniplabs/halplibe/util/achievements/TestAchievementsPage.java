package turniplabs.halplibe.util.achievements;

import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.AchievementList;
import net.minecraft.core.achievement.stat.Stat;
import net.minecraft.core.block.Block;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class TestAchievementsPage extends AchievementPage{

    public static final Achievement TEST = new Achievement(AchievementList.achievementList.size()+1,"test",0,0,Block.glass,null);

    public TestAchievementsPage() {
        super("HalpLibe", "achievements.page.halplibe");
        ((Stat)TEST).registerStat();
        achievementList.add(TEST);
    }

    @Override
    public void getBackground(GuiAchievements guiAchievements, Random random, int iOffset, int jOffset, int blockX1, int blockY1, int blockX2, int blockY2) {
        int l7 = 0;
        while (l7 * 16 - blockY2 < 155) {
            float f5 = 0.6f - (float)(blockY1 + l7) / 25.0f * 0.3f;
            GL11.glColor4f(f5, f5, f5, 1.0f);
            int i8 = 0;
            while (i8 * 16 - blockX2 < 224) {
                guiAchievements.drawTexturedIcon(iOffset + i8 * 16 - blockX2, jOffset + l7 * 16 - blockY2, 16, 16, TextureRegistry.getTexture("minecraft:block/bedrock"));
                ++i8;
            }
            ++l7;
        }
    }
}
