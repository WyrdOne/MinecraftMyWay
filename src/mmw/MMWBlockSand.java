package net.minecraft.src;

import java.util.Random;
import java.lang.reflect.*;

public class MMWBlockSand extends BlockSand {
    public static boolean gravityWorks = true;

    public MMWBlockSand(int par1, int par2) {
        super(par1, par2, Material.sand);
    }

    public MMWBlockSand(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (!par1World.isRemote && gravityWorks) {
            String methodName;
            Method method;
            if (mod_MyWay.isObfuscated) {
                methodName = "l"; // Update when version changes.
            } else {
                methodName = "tryToFall";
            }
            try {
                method = Block.sand.getClass().getDeclaredMethod(methodName, null);
            } catch (Exception ignored) {
                return;
            }
            method.setAccessible(true);
            try {
                method.invoke(Block.sand, new Object[]{par1World, par2, par3, par4, par5Random});
            } catch (Exception ignored) {}
        }
    }
   
}
