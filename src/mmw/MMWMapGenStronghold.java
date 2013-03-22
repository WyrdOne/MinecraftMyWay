package mmw;

import java.util.*;
import java.util.Map.Entry;
import net.minecraft.src.*;

public class MMWMapGenStronghold extends MapGenStronghold {
    public MMWMapGenStronghold() {
    	super();
        switch (mod_MyWay.genStrongholds) {
          	case 1:
          		MMWReflection.setPrivateValue(MapGenStronghold.class, this, "structureCoords", new ChunkCoordIntPair[1]);
          		MMWReflection.setPrivateValue(MapGenStronghold.class, this, "field_82671_h", 64.0D);
          		break;
          	case 3:
          		MMWReflection.setPrivateValue(MapGenStronghold.class, this, "structureCoords", new ChunkCoordIntPair[6]);
          		MMWReflection.setPrivateValue(MapGenStronghold.class, this, "field_82671_h", 16.0D);
          		break;
        }
    }

    public MMWMapGenStronghold(Map par1Map) {
    	super(par1Map);
    }
}
