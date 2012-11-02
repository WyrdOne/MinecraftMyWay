package net.minecraft.src;

import java.util.Random;

public class MMWBlockStone extends BlockStone {
  public int drop = Block.cobblestone.blockID;
   
  public MMWBlockStone(int par1, int par2) {
    super(par1, par2);
  }
  
  public int idDropped(int par1, Random par2Random, int par3) {
    return drop;
  }
}
