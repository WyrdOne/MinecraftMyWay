package mmw;

import net.minecraft.src.*;
import java.util.Random;
import java.lang.reflect.*;

public class MMWBlockSand extends BlockSand {
  public static boolean gravityWorks = true;
  public static Method tryToFall = null;

  public MMWBlockSand(int par1) {
    super(par1, Material.sand);
  }

  public MMWBlockSand(int par1, Material par3Material) {
    super(par1, par3Material);
  }

  public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    if (!par1World.isRemote && gravityWorks) {
    	super.updateTick(par1World, par2, par3, par4, par5Random);
    }
  }
}
