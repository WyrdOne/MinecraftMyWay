package mmw;

import net.minecraft.src.*;

public enum MMWEnumToolMaterial {
  FLINT(1, 100, 3.0F, 1, 5);
  
  /**
  * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = IRON/GOLD)
  */
  private final int harvestLevel;

  /**
  * The number of uses this material allows. (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
  */
  private final int maxUses;

  /**
  * The strength of this tool material against blocks which it is effective against.
  */
  private final float efficiencyOnProperMaterial;

  /** Damage versus entities. */
  private final int damageVsEntity;
  
  /** Defines the natural enchantability factor of the material. */
  private final int enchantability;
  
  private MMWEnumToolMaterial(int par3, int par4, float par5, int par6, int par7) {
    harvestLevel = par3;
    maxUses = par4;
    efficiencyOnProperMaterial = par5;
    damageVsEntity = par6;
    enchantability = par7;
  }

  /**
  * The number of uses this material allows. (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
  */
  public int getMaxUses() {
    return maxUses;
  }

  /**
  * The strength of this tool material against blocks which it is effective against.
  */
  public float getEfficiencyOnProperMaterial() {
    return efficiencyOnProperMaterial;
  }
  
  /**
  * Damage versus entities.
  */
  public int getDamageVsEntity() {
    return damageVsEntity;
  }

  /**
  * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = IRON/GOLD)
  */
  public int getHarvestLevel() {
    return harvestLevel;
  }

  /**
  * Return the natural enchantability factor of the material.
  */
  public int getEnchantability() {
    return enchantability;
  }
  
  /**
   * Return the crafting material for this tool material, used to determine the item that can be used to repair a tool
   * with an anvil
   */
  public int getToolCraftingMaterial() {
    return ((this == FLINT) ? Item.flint.itemID : 0);
  }
}
