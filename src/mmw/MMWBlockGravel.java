package net.minecraft.src;

import java.util.Random;

public class MMWBlockGravel extends MMWBlockSand {
    public MMWBlockGravel(int par1, int par2) {
        super(par1, par2);
    }

    public int idDropped(int par1, Random par2Random, int par3) {
        if (par3 > 3) {
            par3 = 3;
        }

        return par2Random.nextInt(10 - par3 * 3) == 0 ? Item.flint.shiftedIndex : this.blockID;
    }
}
