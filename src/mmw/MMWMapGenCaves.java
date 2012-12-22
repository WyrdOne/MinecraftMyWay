package net.minecraft.src;

import java.util.Random;

public class MMWMapGenCaves extends MapGenCaves
{
    /**
     * Recursively called by generate() (generate) and optionally by itself.
     */
    protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, byte[] par6ArrayOfByte)
    {
        int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
 
        if (this.rand.nextInt((mod_MyWay.genCaves==2) ? 15 : ((mod_MyWay.genCaves==3) ? 30 : 7)) != 0)
        {
            var7 = 0;
        }

        for (int var8 = 0; var8 < var7; ++var8)
        {
            double var9 = (double)(par2 * 16 + this.rand.nextInt(16));
            double var11 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
            double var13 = (double)(par3 * 16 + this.rand.nextInt(16));
            int var15 = 1;

            if (this.rand.nextInt((mod_MyWay.genLargeCaves==2) ? 4 : ((mod_MyWay.genLargeCaves==3) ? 2 : 8)) == 0)
            {
                if (mod_MyWay.genLargeCaves!=0) {
                    this.generateLargeCaveNode(this.rand.nextLong(), par4, par5, par6ArrayOfByte, var9, var11, var13);
                    var15 += this.rand.nextInt(4);
                }
            }

            for (int var16 = 0; var16 < var15; ++var16)
            {
                float var17 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(10) == 0)
                {
                    var19 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                }

                this.generateCaveNode(this.rand.nextLong(), par4, par5, par6ArrayOfByte, var9, var11, var13, var19, var17, var18, 0, 0, 1.0D);
            }
        }
    }
}
