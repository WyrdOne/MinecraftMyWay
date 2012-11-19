package net.minecraft.src;

import java.lang.reflect.*;

public class MMWFoodStats extends FoodStats {
  private int prevFoodLevel = 20;

  /** The player's food timer value. */
  private int foodTimer = 0;

  /** Options for Minecraft My Way */
  public boolean hunger = true;
  public boolean healing = true;

  /**
   * Handles the food game logic.
   */
  public void onUpdate(EntityPlayer par1EntityPlayer) {
    if (!hunger) {
      setFoodLevel(20);
      setFoodSaturationLevel(5.0F);
    } else {
      int var2 = par1EntityPlayer.worldObj.difficultySetting;
      prevFoodLevel = getFoodLevel();
      if (getFoodExhaustionLevel() > 4.0F) {
        addExhaustion(-4.0F);
        if (getSaturationLevel() > 0.0F) {
          setFoodSaturationLevel(Math.max(getSaturationLevel() - 1.0F, 0.0F));
        } else if (var2 > 0) {
          setFoodLevel(Math.max(getFoodLevel() - 1, 0));
        }
      }
      if (healing && getFoodLevel() >= 18 && par1EntityPlayer.shouldHeal()) {
        ++this.foodTimer;
        if (this.foodTimer >= 80) {
          par1EntityPlayer.heal(1);
          this.foodTimer = 0;
        }
      } else if (getFoodLevel() <= 0) {
        ++this.foodTimer;
        if (this.foodTimer >= 80) {
          if (par1EntityPlayer.getHealth() > 10 || var2 >= 3 || par1EntityPlayer.getHealth() > 1 && var2 >= 2) {
              par1EntityPlayer.attackEntityFrom(DamageSource.starve, 1);
          }
          this.foodTimer = 0;
        }
      } else {
        this.foodTimer = 0;
      }
    }
  }

  public int getPrevFoodLevel() {
    return this.prevFoodLevel;
  }

  public void setFoodExhaustionLevel(float foodExhaustionLevel) {
    MMWUtil.setPrivateValue(this.getClass().getSuperclass(), this, "foodExhaustionLevel", "c", foodExhaustionLevel); 
  }

  public float getFoodExhaustionLevel() {
    float foodExhaustionLevel = (Float)MMWUtil.getPrivateValue(this.getClass().getSuperclass(), this, "foodExhaustionLevel", "c"); 
    return foodExhaustionLevel;
  }
}
