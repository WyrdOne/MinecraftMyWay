package net.minecraft.src;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MMWMapGenMineshaft extends MapGenMineshaft
{
    private double field_82673_e = 0.01D;

    public MMWMapGenMineshaft() {
      switch (mod_MyWay.genMineshafts) {
        case 0:
          field_82673_e = 0.0D;
          break;
        case 1:
          field_82673_e = 0.005D;
          break;
        case 3:
          field_82673_e = 0.02D;
          break;
        default:
          field_82673_e = 0.01D;
          break;
      }
    }

    public MMWMapGenMineshaft(Map par1Map)
    {
        Iterator var2 = par1Map.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();

            if (((String)var3.getKey()).equals("chance"))
            {
                this.field_82673_e = MathHelper.parseDoubleWithDefault((String)var3.getValue(), this.field_82673_e);
            }
        }
    }

    protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
        return this.rand.nextDouble() < this.field_82673_e && this.rand.nextInt(80) < Math.max(Math.abs(par1), Math.abs(par2));
    }

    protected StructureStart getStructureStart(int par1, int par2)
    {
        return new StructureMineshaftStart(this.worldObj, this.rand, par1, par2);
    }
}
