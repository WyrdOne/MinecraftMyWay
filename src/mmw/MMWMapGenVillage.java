package mmw;

import java.util.*;
import java.util.Map.Entry;
import net.minecraft.src.*;

public class MMWMapGenVillage extends MapGenVillage {
    public MMWMapGenVillage() {
    	super();
        switch (mod_MyWay.genVillages) {
          	case 1:
                MMWReflection.setPrivateValue(MapGenVillage.class, this, "field_82665_g", 64);
                break;
          	case 3:
          		MMWReflection.setPrivateValue(MapGenVillage.class, this, "field_82665_g", 16);
          		break;
        }
    }

    public MMWMapGenVillage(Map par1Map) {
    	super(par1Map);
    }
}
