package turniplabs.halplibe.util.achievements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptionsButton;
import net.minecraft.client.gui.popup.GuiPopup;
import net.minecraft.client.gui.popup.PopupBuilder;
import net.minecraft.client.gui.popup.PopupCloseListener;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.Scissor;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.AchievementList;
import net.minecraft.core.achievement.stat.StatsCounter;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import turniplabs.halplibe.helper.AchievementHelper;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class GuiAchievements extends GuiScreen {
    private static int top = AchievementList.minY * 24 - 112;
    private static int left = AchievementList.minX * 24 - 112; //- 112
    private static int bottom = AchievementList.maxY * 24 - 77;
    private static int right = AchievementList.maxX * 24 - 77; //- 77
    protected int windowWidth = 256;
    protected int windowHeight = 202;
    protected int mouseX = 0;
    protected int mouseY = 0;
    protected double field_27116_m;
    protected double field_27115_n;
    protected double guiX;
    protected double guiY;
    protected double field_27112_q;
    protected double field_27111_r;
    private int isMouseButtonDown = 0;
    private final StatsCounter statFileWriter;
    public int page = 0;
    GuiScreen parent;

    public GuiAchievements(GuiScreen parent, StatsCounter statFileWriter){
        this.statFileWriter = statFileWriter;
        this.parent = parent;
        this.guiX = field_27112_q = field_27116_m = AchievementList.OPEN_INVENTORY.x * 24 - (double) 141 / 2 - 12;
        this.guiY = field_27111_r = field_27115_n = AchievementList.OPEN_INVENTORY.y * 24 - (double) 141 / 2;
    }

    @Override
    public void init() {
        this.controlList.clear();
        this.controlList.add(new GuiOptionsButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.getInstance().translateKey("gui.achievements.button.done")));
        this.controlList.add(new GuiOptionsButton(2, this.width / 2 - 104, this.height / 2 + 74, 120, 20, "Minecraft"));
    }

    public class PopupListener implements PopupCloseListener {
        @Override
        public void onClosed(int i, Map<String, ?> map) {
            page = (int) map.get("gui.achievements.list");
            controlList.get(1).displayString = Objects.requireNonNull(AchievementHelper.getPage(page)).name;
            if(AchievementHelper.getPage(page) instanceof TestAchievementsPage){
                Minecraft.getMinecraft(Minecraft.class).thePlayer.addStat(TestAchievementsPage.TEST,1);
            }
        }
    }

    @Override
    protected void buttonPressed(GuiButton guibutton) {
        if (guibutton.id == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
        if(guibutton.id == 2) {
            GuiPopup popup = new PopupBuilder(this,128)
                    .withLabel("gui.achievements.label.title")
                    .closeOnClickOut(0)
                    .closeOnEsc(0)
                    .withOnCloseListener(new PopupListener())
                    .withList("gui.achievements.list",Math.min(100,20 * AchievementHelper.getPages().size()),AchievementHelper.getPageNames(),AchievementHelper.getPageDescriptions(),page,true)
                    .build();
            mc.displayGuiScreen(popup);
        }
        super.buttonPressed(guibutton);
    }

    @Override
    public void keyTyped(char c, int i, int mouseX, int mouseY) {
        if (this.mc.gameSettings.keyInventory.isKeyboardKey(i)) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        } else {
            super.keyTyped(c, i, mouseX, mouseY);
        }
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        if (Mouse.isButtonDown(0)) {
            int k = (this.width - this.windowWidth) / 2;
            int l = (this.height - this.windowHeight) / 2;
            int i1 = k + 8;
            int j1 = l + 17;
            if ((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && x >= i1 && x < i1 + 224 && y >= j1 && y < j1 + 155) {
                if (this.isMouseButtonDown == 0) {
                    this.isMouseButtonDown = 1;
                } else {
                    this.guiX -= x - this.mouseX;
                    this.guiY -= y - this.mouseY;
                    this.field_27112_q = this.field_27116_m = this.guiX;
                    this.field_27111_r = this.field_27115_n = this.guiY;
                }
                this.mouseX = x;
                this.mouseY = y;
            }
            if (this.field_27112_q < (double) left) {
                this.field_27112_q = left;
            }
            if (this.field_27111_r < (double) top) {
                this.field_27111_r = top;
            }
            if (this.field_27112_q >= (double) right) {
                this.field_27112_q = right - 1;
            }
            if (this.field_27111_r >= (double) bottom) {
                this.field_27111_r = bottom - 1;
            }
        } else {
            this.isMouseButtonDown = 0;
        }
        this.drawDefaultBackground();
        drawAchievementBackground(x,y,renderPartialTicks);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        drawForeground();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
    }

    @Override
    public void tick() {
        this.field_27116_m = this.guiX;
        this.field_27115_n = this.guiY;
        double var1 = this.field_27112_q - this.guiX;
        double var3 = this.field_27111_r - this.guiY;
        if (var1 * var1 + var3 * var3 < 4.0) {
            this.guiX += var1;
            this.guiY += var3;
        } else {
            this.guiX += var1 * 0.85;
            this.guiY += var3 * 0.85;
        }
    }

    private void drawForeground() {
        int x = (this.width - this.windowWidth) / 2;
        int y = (this.height - this.windowHeight) / 2;
        this.fontRenderer.drawString(I18n.getInstance().translateKey("gui.achievements.label.title"), x + 15, y + 5, 4210752);
    }

    public void drawAchievementBackground(int x, int y, float renderPartialTicks) {
        int posX = MathHelper.floor_double(this.field_27116_m + (this.guiX - this.field_27116_m) * (double)renderPartialTicks);
        int posY = MathHelper.floor_double(this.field_27115_n + (this.guiY - this.field_27115_n) * (double)renderPartialTicks);

        if(posX < left){
            posX = left;
        }
        if(posY < top){
            posY = top;
        }
        if(posX > right){
            posX = right;
        }
        if(posY > bottom){
            posY = bottom;
        }
        int blockTex = this.mc.renderEngine.getTexture("/terrain.png");
        int bgTex = this.mc.renderEngine.getTexture("/achievement/bg.png");
        int i = (this.width - this.windowWidth) / 2;
        int j = (this.height - this.windowHeight) / 2;
        int iOffset = i + 16;
        int jOffset = j + 17;
        this.zLevel = 0;
        GL11.glDepthFunc(518);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        GL11.glEnable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        Scissor.enable(i + 1, j + 1, windowWidth - 2, windowHeight - 2);
        this.mc.renderEngine.bindTexture(blockTex);
        AchievementPage achievementPage = AchievementHelper.getPage(this.page);
        if(achievementPage != null){
            int blockX1 = posX + 288 >> 4;
            int blockY1 = posY + 288 >> 4;
            int blockX2 = (posX + 288) % 16;
            int blockY2 = (posY + 288) % 16;
            Random random = new Random();
            achievementPage.getBackground(this,random,iOffset,jOffset,blockX1,blockY1,blockX2,blockY2);
        }
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glDisable(3553);
        if (achievementPage != null) {
            for (Achievement achievement : achievementPage.achievementList) {
                if(achievement.parent == null) {
                    int x1 = achievement.x * 24 - posX + 11 + iOffset;
                    int y1 = achievement.y * 24 - posY + 11 + jOffset;
                    int color = 0;
                    boolean isUnlocked = statFileWriter.isAchievementUnlocked(achievement);
                    boolean isAvailable = statFileWriter.isAchievementUnlockable(achievement);
                    int n = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0 * Math.PI * 2.0) <= 0.6 ? 130 : 255;
                    color = isUnlocked ? -9408400 : (isAvailable ? 65280 + (n << 24) : -16777216);
                    this.drawLineHorizontal(x1, x1, y1, color);
                    this.drawLineVertical(x1, y1, y1, color);
                } else {
                    int x1 = achievement.x * 24 - posX + 11 + iOffset;
                    int y1 = achievement.y * 24 - posY + 11 + jOffset;
                    int x2 = achievement.parent.x * 24 - posX + 11 + iOffset;
                    int y2 = achievement.parent.y * 24 - posY + 11 + jOffset;
                    int color = 0;
                    boolean isUnlocked = statFileWriter.isAchievementUnlocked(achievement);
                    boolean isAvailable = statFileWriter.isAchievementUnlockable(achievement);
                    int n = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0 * Math.PI * 2.0) <= 0.6 ? 130 : 255;
                    color = isUnlocked ? -9408400 : (isAvailable ? 65280 + (n << 24) : -16777216);
                    this.drawLineHorizontal(x1, x2, y1, color);
                    this.drawLineVertical(x2, y1, y2, color);
                }
            }
            Achievement achievement1 = null;
            ItemEntityRenderer renderitem = new ItemEntityRenderer();
            Lighting.enableInventoryLight();
            GL11.glDisable(2896);
            GL11.glEnable(32826);
            GL11.glEnable(2903);
            for (Achievement achievement : achievementPage.achievementList) {
                int x1 = achievement.x * 24 - posX;
                int y1 = achievement.y * 24 - posY;
                if (x1 < -24 || y1 < -24 || x1 > 224 || y1 > 155) continue;
                boolean isUnlocked = statFileWriter.isAchievementUnlocked(achievement);
                boolean isAvailable = statFileWriter.isAchievementUnlockable(achievement);
                if(isUnlocked){
                    GL11.glColor4f(1,1,1,1);
                } else if (isAvailable) {
                    float f = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0 * Math.PI * 2.0) >= 0.6 ? 0.8f : 0.6f;
                    GL11.glColor4f(f, f, f, 1.0f);
                } else {
                    GL11.glColor4f(0.3f,0.3f,0.3f,1);
                }
                this.mc.renderEngine.bindTexture(bgTex);
                int x2 = iOffset + x1;
                int y2 = jOffset + y1;
                if (achievement.getSpecial()) {
                    this.drawTexturedModalRect(x2 - 2, y2 - 2, 26, 202, 26, 26);
                } else {
                    this.drawTexturedModalRect(x2 - 2, y2 - 2, 0, 202, 26, 26);
                }
                if(!isAvailable){
                    GL11.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
                    renderitem.field_27004_a = false;
                }
                GL11.glEnable(2896);
                GL11.glEnable(2884);
                renderitem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(achievement.itemId, 1, 0), x2 + 3, y2 + 3, 1.0f);
                GL11.glDisable(2896);
                if(!isAvailable){
                    renderitem.field_27004_a = true;
                }
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                if (x < iOffset || y < jOffset || x >= iOffset + 224 || y >= jOffset + 155 || x < x2 || x > x2 + 22 || y < y2 || y > y2 + 22) continue;
                achievement1 = achievement;
            }
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Scissor.disable();
            bgTex = this.mc.renderEngine.getTexture("/achievement/bg.png");
            this.mc.renderEngine.bindTexture(bgTex);
            this.drawTexturedModalRect(i, j, 0, 0, this.windowWidth, this.windowHeight);
            GL11.glPopMatrix();
            this.zLevel = 0.0f;
            GL11.glDepthFunc(515);
            GL11.glDisable(2929);
            GL11.glEnable(3553);
            super.drawScreen(x, y, renderPartialTicks);
            if(achievement1 != null){
                String name = achievement1.getStatName();
                String desc = achievement1.getDescription();
                int x1 = x + 12;
                int y1 = y - 4;
                boolean isUnlocked = statFileWriter.isAchievementUnlocked(achievement1);
                boolean isAvailable = statFileWriter.isAchievementUnlockable(achievement1);
                if(isAvailable){
                    int maxNameLength = Math.max(this.fontRenderer.getStringWidth(name), 120);
                    int descLength = this.fontRenderer.func_27277_a(desc, maxNameLength);
                    if(isUnlocked){
                        descLength += 12;
                    }
                    this.drawGradientRect(x1 - 3, y1 - 3, x1 + maxNameLength + 3, y1 + descLength + 3 + 12, -1073741824, -1073741824);
                    this.fontRenderer.func_27278_a(desc, x1, y1 + 12, maxNameLength, -6250336);
                    if(isUnlocked){
                        this.fontRenderer.drawStringWithShadow(I18n.getInstance().translateKey("achievement.taken"), x1, y1 + descLength + 4, -7302913);
                    }
                } else {
                    int maxNameLength = Math.max(this.fontRenderer.getStringWidth(name), 120);
                    String requires = I18n.getInstance().translateKeyAndFormat("achievement.requires", achievement1.parent.getStatName());
                    int k7 = this.fontRenderer.func_27277_a(requires, maxNameLength);
                    this.drawGradientRect(x1 - 3, y1 - 3, x1 + maxNameLength + 3, y1 + k7 + 12 + 3, -1073741824, -1073741824);
                    this.fontRenderer.func_27278_a(requires, x1, y1 + 12, maxNameLength, -9416624);
                }
                this.fontRenderer.drawStringWithShadow(name, x1, y1, isAvailable ? (achievement1.getSpecial() ? -128 : -1) : (achievement1.getSpecial()? -8355776 : -8355712));
            }
            GL11.glEnable(2929);
            GL11.glEnable(2896);
            Lighting.disable();
        }
    }
}