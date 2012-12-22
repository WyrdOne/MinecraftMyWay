package net.minecraft.src;

import java.awt.Color;
import java.lang.reflect.*;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class MMWGuiIngame extends GuiIngame {
  public static boolean optXP = true;
  public static boolean optHunger = true;
  private final Random rand = new Random();
  private final Minecraft mc;
  private final GuiNewChat persistantChatGUI;
  private static Method method_renderBossHealth = null;
  private static Method method_renderPumpkinBlur = null;
  private static Method method_renderVignette = null;
  private static Method method_renderPortalOverlay = null;
  private static Method method_renderInventorySlot = null;

  public MMWGuiIngame(Minecraft par1Minecraft) {
    super(par1Minecraft);
    persistantChatGUI = getChatGUI();
    mc = par1Minecraft;
    method_renderBossHealth = MMWReflection.getPrivateMethod(GuiIngame.class, "renderBossHealth");
    method_renderPumpkinBlur = MMWReflection.getPrivateMethod(GuiIngame.class, "renderPumpkinBlur", int.class, int.class);
    method_renderVignette = MMWReflection.getPrivateMethod(GuiIngame.class, "renderVignette", float.class, int.class, int.class);
    method_renderPortalOverlay = MMWReflection.getPrivateMethod(GuiIngame.class, "renderPortalOverlay", float.class, int.class, int.class);
    method_renderInventorySlot = MMWReflection.getPrivateMethod(GuiIngame.class, "renderInventorySlot", int.class, int.class, int.class, float.class);
  }
  
  private void renderBossHealth() {
    try {
      method_renderBossHealth.invoke(this);
    } catch (Exception ignored) { }
  }
  
  private void renderPumpkinBlur(int par1, int par2) {
    try {
      method_renderPumpkinBlur.invoke(this, par1, par2);
    } catch (Exception ignored) { }
  }

  private void renderVignette(float par1, int par2, int par3) {
    try {
      method_renderVignette.invoke(this, par1, par2, par3);
    } catch (Exception ignored) { }
  }

  private void renderPortalOverlay(float par1, int par2, int par3) {
    try {
      method_renderPortalOverlay.invoke(this, par1, par2, par3);
    } catch (Exception ignored) { }
  }
  
  private void renderInventorySlot(int par1, int par2, int par3, float par4) {
    try {
      method_renderInventorySlot.invoke(this, par1, par2, par3, par4);
    } catch (Exception ignored) { }
  }

  private String recordPlaying() {
    return (String)MMWReflection.getPrivateValue(GuiIngame.class, this, "recordPlaying");
  }
  
  private int recordPlayingUpFor() {
    return ((Integer)MMWReflection.getPrivateValue(GuiIngame.class, this, "recordPlayingUpFor")).intValue();
  }
  
  private boolean recordIsPlaying() {
    return ((Boolean)MMWReflection.getPrivateValue(GuiIngame.class, this, "recordIsPlaying")).booleanValue();
  }

    /**
     * Render the ingame overlay with quick icon bar, ...
     */
    public void renderGameOverlay(float par1, boolean par2, int par3, int par4)
    {
        ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var6 = var5.getScaledWidth();
        int var7 = var5.getScaledHeight();
        FontRenderer var8 = this.mc.fontRenderer;
        this.mc.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND);

        if (Minecraft.isFancyGraphicsEnabled())
        {
            this.renderVignette(this.mc.thePlayer.getBrightness(par1), var6, var7);
        }
        else
        {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        ItemStack var9 = this.mc.thePlayer.inventory.armorItemInSlot(3);

        if (this.mc.gameSettings.thirdPersonView == 0 && var9 != null && var9.itemID == Block.pumpkin.blockID)
        {
            this.renderPumpkinBlur(var6, var7);
        }

        if (!this.mc.thePlayer.isPotionActive(Potion.confusion))
        {
            float var10 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * par1;

            if (var10 > 0.0F)
            {
                this.renderPortalOverlay(var10, var6, var7);
            }
        }

        boolean var11;
        int var12;
        int var13;
        int var17;
        int var16;
        int var19;
        int var20;
        int var23;
        int var22;
        int var24;
        int var47;

        if (!this.mc.playerController.func_78747_a())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
            InventoryPlayer var31 = this.mc.thePlayer.inventory;
            this.zLevel = -90.0F;
            this.drawTexturedModalRect(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(var6 / 2 - 91 - 1 + var31.currentItem * 20, var7 - 22 - 1, 0, 22, 24, 22);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
            this.drawTexturedModalRect(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
            GL11.glDisable(GL11.GL_BLEND);
            var11 = this.mc.thePlayer.hurtResistantTime / 3 % 2 == 1;

            if (this.mc.thePlayer.hurtResistantTime < 10)
            {
                var11 = false;
            }

            var12 = this.mc.thePlayer.getHealth();
            var13 = this.mc.thePlayer.prevHealth;
            this.rand.setSeed((long)(getUpdateCounter() * 312871));
            boolean var14 = false;
            FoodStats var15 = this.mc.thePlayer.getFoodStats();
            var16 = var15.getFoodLevel();
            var17 = var15.getPrevFoodLevel();
            this.mc.mcProfiler.startSection("bossHealth");
            this.renderBossHealth();
            this.mc.mcProfiler.endSection();
            int var18;

            if (this.mc.playerController.shouldDrawHUD())
            {
                var18 = var6 / 2 - 91;
                var19 = var6 / 2 + 91;
                this.mc.mcProfiler.startSection("expBar");
                var20 = this.mc.thePlayer.xpBarCap();

/* MMW */       if (optXP && (var20 > 0))
                {
                    short var21 = 182;
                    var22 = (int)(this.mc.thePlayer.experience * (float)(var21 + 1));
                    var23 = var7 - 32 + 3;
                    this.drawTexturedModalRect(var18, var23, 0, 64, var21, 5);

                    if (var22 > 0)
                    {
                        this.drawTexturedModalRect(var18, var23, 0, 69, var22, 5);
                    }
                }

/*MMW*/         var47 = var7 - 39 + ((optXP) ? 0 : 8);
                var22 = var47 - 10;
                var23 = this.mc.thePlayer.getTotalArmorValue();
                var24 = -1;

                if (this.mc.thePlayer.isPotionActive(Potion.regeneration))
                {
                    var24 = getUpdateCounter() % 25;
                }

                this.mc.mcProfiler.endStartSection("healthArmor");
                int var25;
                int var26;
                int var29;
                int var28;

                for (var25 = 0; var25 < 10; ++var25)
                {
                    if (var23 > 0)
                    {
/*MMW*/                 if (optHunger) {
/*MMW*/                   var26 = var18 + var25 * 8;
/*MMW*/                 } else {
/*MMW*/                   var26 = var19 - var25 * 8 - 9;
/*MMW*/                 }

                        if (var25 * 2 + 1 < var23)
                        {
/*MMW*/                     this.drawTexturedModalRect(var26, var22 + ((optHunger) ? 0 : 10), 34, 9, 9, 9);
                        }

                        if (var25 * 2 + 1 == var23)
                        {
/*MMW*/                     this.drawTexturedModalRect(var26, var22 + ((optHunger) ? 0 : 10), 25, 9, 9, 9);
                        }

                        if (var25 * 2 + 1 > var23)
                        {
/*MMW*/                     this.drawTexturedModalRect(var26, var22 + ((optHunger) ? 0 : 10), 16, 9, 9, 9);
                        }
                    }

                    var26 = 16;

                    if (this.mc.thePlayer.isPotionActive(Potion.poison))
                    {
                        var26 += 36;
                    }
                    else if (this.mc.thePlayer.isPotionActive(Potion.wither))
                    {
                        var26 += 72;
                    }

                    byte var27 = 0;

                    if (var11)
                    {
                        var27 = 1;
                    }

                    var28 = var18 + var25 * 8;
                    var29 = var47;

                    if (var12 <= 4)
                    {
                        var29 = var47 + this.rand.nextInt(2);
                    }

                    if (var25 == var24)
                    {
                        var29 -= 2;
                    }

                    byte var30 = 0;

                    if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                    {
                        var30 = 5;
                    }

                    this.drawTexturedModalRect(var28, var29, 16 + var27 * 9, 9 * var30, 9, 9);

                    if (var11)
                    {
                        if (var25 * 2 + 1 < var13)
                        {
                            this.drawTexturedModalRect(var28, var29, var26 + 54, 9 * var30, 9, 9);
                        }

                        if (var25 * 2 + 1 == var13)
                        {
                            this.drawTexturedModalRect(var28, var29, var26 + 63, 9 * var30, 9, 9);
                        }
                    }

                    if (var25 * 2 + 1 < var12)
                    {
                        this.drawTexturedModalRect(var28, var29, var26 + 36, 9 * var30, 9, 9);
                    }

                    if (var25 * 2 + 1 == var12)
                    {
                        this.drawTexturedModalRect(var28, var29, var26 + 45, 9 * var30, 9, 9);
                    }
                }

                this.mc.mcProfiler.endStartSection("food");
                int var51;
if (optHunger) {
                for (var25 = 0; var25 < 10; ++var25)
                {
                    var26 = var47;
                    var51 = 16;
                    byte var52 = 0;

                    if (this.mc.thePlayer.isPotionActive(Potion.hunger))
                    {
                        var51 += 36;
                        var52 = 13;
                    }

                    if (this.mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && getUpdateCounter() % (var16 * 3 + 1) == 0)
                    {
                        var26 = var47 + (this.rand.nextInt(3) - 1);
                    }

                    if (var14)
                    {
                        var52 = 1;
                    }

                    var29 = var19 - var25 * 8 - 9;
                    this.drawTexturedModalRect(var29, var26, 16 + var52 * 9, 27, 9, 9);

                    if (var14)
                    {
                        if (var25 * 2 + 1 < var17)
                        {
                            this.drawTexturedModalRect(var29, var26, var51 + 54, 27, 9, 9);
                        }

                        if (var25 * 2 + 1 == var17)
                        {
                            this.drawTexturedModalRect(var29, var26, var51 + 63, 27, 9, 9);
                        }
                    }

                    if (var25 * 2 + 1 < var16)
                    {
                        this.drawTexturedModalRect(var29, var26, var51 + 36, 27, 9, 9);
                    }

                    if (var25 * 2 + 1 == var16)
                    {
                        this.drawTexturedModalRect(var29, var26, var51 + 45, 27, 9, 9);
                    }
                }
}
                this.mc.mcProfiler.endStartSection("air");

                if (this.mc.thePlayer.isInsideOfMaterial(Material.water))
                {
                    var25 = this.mc.thePlayer.getAir();
                    var26 = MathHelper.ceiling_double_int((double)(var25 - 2) * 10.0D / 300.0D);
                    var51 = MathHelper.ceiling_double_int((double)var25 * 10.0D / 300.0D) - var26;

                    for (var28 = 0; var28 < var26 + var51; ++var28)
                    {
                        if (var28 < var26)
                        {
                            this.drawTexturedModalRect(var19 - var28 * 8 - 9, var22, 16, 18, 9, 9);
                        }
                        else
                        {
                            this.drawTexturedModalRect(var19 - var28 * 8 - 9, var22, 25, 18, 9, 9);
                        }
                    }
                }

                this.mc.mcProfiler.endSection();
            }

            GL11.glDisable(GL11.GL_BLEND);
            this.mc.mcProfiler.startSection("actionBar");
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (var18 = 0; var18 < 9; ++var18)
            {
                var19 = var6 / 2 - 90 + var18 * 20 + 2;
                var20 = var7 - 16 - 3;
                this.renderInventorySlot(var18, var19, var20, par1);
            }

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            this.mc.mcProfiler.endSection();
        }

        float var33;

        if (this.mc.thePlayer.getSleepTimer() > 0)
        {
            this.mc.mcProfiler.startSection("sleep");
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            int var32 = this.mc.thePlayer.getSleepTimer();
            var33 = (float)var32 / 100.0F;

            if (var33 > 1.0F)
            {
                var33 = 1.0F - (float)(var32 - 100) / 10.0F;
            }

            var12 = (int)(220.0F * var33) << 24 | 1052704;
            drawRect(0, 0, var6, var7, var12);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            this.mc.mcProfiler.endSection();
        }

        int var38;
        int var40;

        if (this.mc.playerController.func_78763_f() && this.mc.thePlayer.experienceLevel > 0)
        {
            this.mc.mcProfiler.startSection("expLevel");
            var11 = false;
            var12 = var11 ? 16777215 : 8453920;
            String var35 = "" + this.mc.thePlayer.experienceLevel;
            var40 = (var6 - var8.getStringWidth(var35)) / 2;
            var38 = var7 - 31 - 4;
            var8.drawString(var35, var40 + 1, var38, 0);
            var8.drawString(var35, var40 - 1, var38, 0);
            var8.drawString(var35, var40, var38 + 1, 0);
            var8.drawString(var35, var40, var38 - 1, 0);
            var8.drawString(var35, var40, var38, var12);
            this.mc.mcProfiler.endSection();
        }

        if (this.mc.isDemo())
        {
            this.mc.mcProfiler.startSection("demo");
            String var36 = "";

            if (this.mc.theWorld.getTotalWorldTime() >= 120500L)
            {
                var36 = StatCollector.translateToLocal("demo.demoExpired");
            }
            else
            {
                var36 = String.format(StatCollector.translateToLocal("demo.remainingTime"), new Object[] {StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.getTotalWorldTime()))});
            }

            var12 = var8.getStringWidth(var36);
            var8.drawStringWithShadow(var36, var6 - var12 - 10, 5, 16777215);
            this.mc.mcProfiler.endSection();
        }

        if (this.mc.gameSettings.showDebugInfo)
        {
            this.mc.mcProfiler.startSection("debug");
            GL11.glPushMatrix();
            var8.drawStringWithShadow("Minecraft 1.4.4 (" + this.mc.debug + ")", 2, 2, 16777215);
            var8.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 12, 16777215);
            var8.drawStringWithShadow(this.mc.getEntityDebug(), 2, 22, 16777215);
            var8.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 32, 16777215);
            var8.drawStringWithShadow(this.mc.getWorldProviderName(), 2, 42, 16777215);
            long var41 = Runtime.getRuntime().maxMemory();
            long var34 = Runtime.getRuntime().totalMemory();
            long var42 = Runtime.getRuntime().freeMemory();
            long var43 = var34 - var42;
            String var45 = "Used memory: " + var43 * 100L / var41 + "% (" + var43 / 1024L / 1024L + "MB) of " + var41 / 1024L / 1024L + "MB";
            this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, 2, 14737632);
            var45 = "Allocated memory: " + var34 * 100L / var41 + "% (" + var34 / 1024L / 1024L + "MB)";
            this.drawString(var8, var45, var6 - var8.getStringWidth(var45) - 2, 12, 14737632);
            var47 = MathHelper.floor_double(this.mc.thePlayer.posX);
            var22 = MathHelper.floor_double(this.mc.thePlayer.posY);
            var23 = MathHelper.floor_double(this.mc.thePlayer.posZ);
            this.drawString(var8, String.format("x: %.5f (%d) // c: %d (%d)", new Object[] {Double.valueOf(this.mc.thePlayer.posX), Integer.valueOf(var47), Integer.valueOf(var47 >> 4), Integer.valueOf(var47 & 15)}), 2, 64, 14737632);
            this.drawString(var8, String.format("y: %.3f (feet pos, %.3f eyes pos)", new Object[] {Double.valueOf(this.mc.thePlayer.boundingBox.minY), Double.valueOf(this.mc.thePlayer.posY)}), 2, 72, 14737632);
            this.drawString(var8, String.format("z: %.5f (%d) // c: %d (%d)", new Object[] {Double.valueOf(this.mc.thePlayer.posZ), Integer.valueOf(var23), Integer.valueOf(var23 >> 4), Integer.valueOf(var23 & 15)}), 2, 80, 14737632);
            var24 = MathHelper.floor_double((double)(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            this.drawString(var8, "f: " + var24 + " (" + Direction.directions[var24] + ") / " + MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw), 2, 88, 14737632);

            if (this.mc.theWorld != null && this.mc.theWorld.blockExists(var47, var22, var23))
            {
                Chunk var53 = this.mc.theWorld.getChunkFromBlockCoords(var47, var23);
                this.drawString(var8, "lc: " + (var53.getTopFilledSegment() + 15) + " b: " + var53.getBiomeGenForWorldCoords(var47 & 15, var23 & 15, this.mc.theWorld.getWorldChunkManager()).biomeName + " bl: " + var53.getSavedLightValue(EnumSkyBlock.Block, var47 & 15, var22, var23 & 15) + " sl: " + var53.getSavedLightValue(EnumSkyBlock.Sky, var47 & 15, var22, var23 & 15) + " rl: " + var53.getBlockLightValue(var47 & 15, var22, var23 & 15, 0), 2, 96, 14737632);
            }

            this.drawString(var8, String.format("ws: %.3f, fs: %.3f, g: %b, fl: %d", new Object[] {Float.valueOf(this.mc.thePlayer.capabilities.getWalkSpeed()), Float.valueOf(this.mc.thePlayer.capabilities.getFlySpeed()), Boolean.valueOf(this.mc.thePlayer.onGround), Integer.valueOf(this.mc.theWorld.getHeightValue(var47, var23))}), 2, 104, 14737632);
            GL11.glPopMatrix();
            this.mc.mcProfiler.endSection();
        }

        if (recordPlayingUpFor() > 0)
        {
            this.mc.mcProfiler.startSection("overlayMessage");
            var33 = (float)recordPlayingUpFor() - par1;
            var12 = (int)(var33 * 256.0F / 20.0F);

            if (var12 > 255)
            {
                var12 = 255;
            }

            if (var12 > 0)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(var6 / 2), (float)(var7 - 48), 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                var13 = 16777215;

                if (recordIsPlaying())
                {
                    var13 = Color.HSBtoRGB(var33 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                var8.drawString(recordPlaying(), -var8.getStringWidth(recordPlaying()) / 2, -4, var13 + (var12 << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, (float)(var7 - 48), 0.0F);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(getUpdateCounter());
        this.mc.mcProfiler.endSection();
        GL11.glPopMatrix();

        if (this.mc.gameSettings.keyBindPlayerList.pressed && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.playerInfoList.size() > 1))
        {
            this.mc.mcProfiler.startSection("playerList");
            NetClientHandler var37 = this.mc.thePlayer.sendQueue;
            List var39 = var37.playerInfoList;
            var13 = var37.currentServerMaxPlayers;
            var40 = var13;

            for (var38 = 1; var40 > 20; var40 = (var13 + var38 - 1) / var38)
            {
                ++var38;
            }

            var16 = 300 / var38;

            if (var16 > 150)
            {
                var16 = 150;
            }

            var17 = (var6 - var38 * var16) / 2;
            byte var44 = 10;
            drawRect(var17 - 1, var44 - 1, var17 + var16 * var38, var44 + 9 * var40, Integer.MIN_VALUE);

            for (var19 = 0; var19 < var13; ++var19)
            {
                var20 = var17 + var19 % var38 * var16;
                var47 = var44 + var19 / var38 * 9;
                drawRect(var20, var47, var20 + var16 - 1, var47 + 8, 553648127);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (var19 < var39.size())
                {
                    GuiPlayerInfo var46 = (GuiPlayerInfo)var39.get(var19);
                    var8.drawStringWithShadow(var46.name, var20, var47, 16777215);
                    this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/icons.png"));
                    byte var50 = 0;
                    boolean var48 = false;
                    byte var49;

                    if (var46.responseTime < 0)
                    {
                        var49 = 5;
                    }
                    else if (var46.responseTime < 150)
                    {
                        var49 = 0;
                    }
                    else if (var46.responseTime < 300)
                    {
                        var49 = 1;
                    }
                    else if (var46.responseTime < 600)
                    {
                        var49 = 2;
                    }
                    else if (var46.responseTime < 1000)
                    {
                        var49 = 3;
                    }
                    else
                    {
                        var49 = 4;
                    }

                    this.zLevel += 100.0F;
                    this.drawTexturedModalRect(var20 + var16 - 12, var47, 0 + var50 * 10, 176 + var49 * 8, 10, 8);
                    this.zLevel -= 100.0F;
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }
}
