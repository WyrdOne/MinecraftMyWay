package net.minecraft.src;

import java.util.Random;

public class MMWItemDye extends ItemDye {
  public static boolean disableBonemeal = false;

  public MMWItemDye(int par1) {
    super(par1);
  }

  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
    if (par1ItemStack.getItemDamage() != 15) { // Not Bonemeal
      return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
    } else {
      // Bonemeal
      if (!disableBonemeal) {
        return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
      }
    }
    return false;
  }
}
