package net.minecraft.src;

import java.util.Random;
import java.lang.reflect.*;

public class MMWBlockSand extends BlockSand {
  public static boolean gravityWorks = true;
  public static Method tryToFall = null;

  public MMWBlockSand(int par1, int par2) {
    super(par1, par2, Material.sand);
  }

  public MMWBlockSand(int par1, int par2, Material par3Material) {
    super(par1, par2, par3Material);
  }

  public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    if (!par1World.isRemote && gravityWorks) {
      if (tryToFall==null) {
        tryToFall = MMWReflection.getPrivateMethod(Block.sand.getClass(), "tryToFall", World.class, int.class, int.class, int.class);
      }          
      tryToFall.setAccessible(true);
      try {
        tryToFall.invoke(Block.sand, par1World, par2, par3, par4);
      } catch (Exception ignored) { }
    }
  }
}
