package net.minecraft.src;

import java.util.Random;

public class MMWMapGenRavine extends MapGenRavine
{
    /**
     * Recursively called by generate() (generate) and optionally by itself.
     */
    protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, byte[] par6ArrayOfByte)
    {
        if (mod_MyWay.genRavines==0) {
            return;
        }
        if (this.rand.nextInt(((mod_MyWay.genRavines==2) ? 50 : ((mod_MyWay.genRavines==3) ? 25 : 100))) == 0)
        {
            double var7 = (double)(par2 * 16 + this.rand.nextInt(16));
            double var9 = (double)(this.rand.nextInt(this.rand.nextInt(40) + 8) + 20);
            double var11 = (double)(par3 * 16 + this.rand.nextInt(16));
            byte var13 = 1;

            for (int var14 = 0; var14 < var13; ++var14)
            {
                float var15 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var16 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var17 = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
                this.generateRavine(this.rand.nextLong(), par4, par5, par6ArrayOfByte, var7, var9, var11, var17, var15, var16, 0, 0, 3.0D);
            }
        }
    }
}
