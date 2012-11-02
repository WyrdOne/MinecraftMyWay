package net.minecraft.src;

import java.util.Random;

public class MMWItemDye extends ItemDye {
  public static boolean disableBonemeal = false;

  public MMWItemDye(int par1) {
    super(par1);
  }

  public boolean tryPlaceIntoWorld(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
    if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6)) {
      return false;
    } else {
      int var11;
      int var12;

      if (!disableBonemeal && par1ItemStack.getItemDamage() == 15) {
        var11 = par3World.getBlockId(par4, par5, par6);
        if (var11 == Block.sapling.blockID) {
          if (!par3World.isRemote) {
            ((BlockSapling)Block.sapling).growTree(par3World, par4, par5, par6, par3World.rand);
            --par1ItemStack.stackSize;
          }
          return true;
        }
        if (var11 == Block.mushroomBrown.blockID || var11 == Block.mushroomRed.blockID) {
          if (!par3World.isRemote && ((BlockMushroom)Block.blocksList[var11]).fertilizeMushroom(par3World, par4, par5, par6, par3World.rand)) {
            --par1ItemStack.stackSize;
          }
          return true;
        }
        if (var11 == Block.melonStem.blockID || var11 == Block.pumpkinStem.blockID) {
          if (par3World.getBlockMetadata(par4, par5, par6) == 7) {
            return false;
          }
          if (!par3World.isRemote) {
            ((BlockStem)Block.blocksList[var11]).fertilizeStem(par3World, par4, par5, par6);
            --par1ItemStack.stackSize;
          }
          return true;
        }
        if (var11 == Block.crops.blockID) {
          if (par3World.getBlockMetadata(par4, par5, par6) == 7) {
            return false;
          }
          if (!par3World.isRemote) {
            ((BlockCrops)Block.crops).fertilize(par3World, par4, par5, par6);
            --par1ItemStack.stackSize;
          }
          return true;
        }
        if (var11 == Block.cocoa.blockID) {
          if (!par3World.isRemote) {
            par3World.setBlockMetadataWithNotify(par4, par5, par6, 8 | BlockDirectional.getDirection(par3World.getBlockMetadata(par4, par5, par6)));
            --par1ItemStack.stackSize;
          }
          return true;
        }
        if (var11 == Block.grass.blockID) {
          if (!par3World.isRemote) {
            --par1ItemStack.stackSize;
label135:
            for (var12 = 0; var12 < 128; ++var12) {
              int var13 = par4;
              int var14 = par5 + 1;
              int var15 = par6;

              for (int var16 = 0; var16 < var12 / 16; ++var16) {
                var13 += itemRand.nextInt(3) - 1;
                var14 += (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2;
                var15 += itemRand.nextInt(3) - 1;

                if (par3World.getBlockId(var13, var14 - 1, var15) != Block.grass.blockID || par3World.isBlockNormalCube(var13, var14, var15)) {
                  continue label135;
                }
              }
              if (par3World.getBlockId(var13, var14, var15) == 0) {
                if (itemRand.nextInt(10) != 0) {
                  if (Block.tallGrass.canBlockStay(par3World, var13, var14, var15)) {
                    par3World.setBlockAndMetadataWithNotify(var13, var14, var15, Block.tallGrass.blockID, 1);
                  }
                }
                else if (itemRand.nextInt(3) != 0)
                {
                  if (Block.plantYellow.canBlockStay(par3World, var13, var14, var15)) {
                    par3World.setBlockWithNotify(var13, var14, var15, Block.plantYellow.blockID);
                  }
                }
                else if (Block.plantRed.canBlockStay(par3World, var13, var14, var15)) {
                  par3World.setBlockWithNotify(var13, var14, var15, Block.plantRed.blockID);
                }
              }
            }
          }
          return true;
        }
      } else if (par1ItemStack.getItemDamage() == 3) {
        var11 = par3World.getBlockId(par4, par5, par6);
        var12 = par3World.getBlockMetadata(par4, par5, par6);

        if (var11 == Block.wood.blockID && BlockLog.limitToValidMetadata(var12) == 3) {
          if (par7 == 0) {
            return false;
          }
          if (par7 == 1) {
            return false;
          }
          if (par7 == 2) {
            --par6;
          }
          if (par7 == 3) {
            ++par6;
          }
          if (par7 == 4) {
            --par4;
          }
          if (par7 == 5) {
            ++par4;
          }
          if (par3World.isAirBlock(par4, par5, par6)) {
            par3World.setBlockWithNotify(par4, par5, par6, Block.cocoa.blockID);
            if (par3World.getBlockId(par4, par5, par6) == Block.cocoa.blockID) {
              Block.blocksList[Block.cocoa.blockID].updateBlockMetadata(par3World, par4, par5, par6, par7, par8, par9, par10);
            }
            if (!par2EntityPlayer.capabilities.isCreativeMode) {
              --par1ItemStack.stackSize;
            }
          }
          return true;
        }
      }
      return false;
    }
  }
}
