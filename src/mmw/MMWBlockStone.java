package mmw;

import net.minecraft.src.*;
import java.util.Random;

public class MMWBlockStone extends BlockStone {
  public int drop = Block.cobblestone.blockID;
   
  public MMWBlockStone(int par1) {
    super(par1);
  }
  
  public int idDropped(int par1, Random par2Random, int par3) {
    if (drop!=Block.gravel.blockID) {
      return drop;
    }
    if (par3 > 3) {
      par3 = 3;
    }
    return par2Random.nextInt(10 - par3 * 3) == 0 ? Item.flint.itemID : drop;
  }
}
