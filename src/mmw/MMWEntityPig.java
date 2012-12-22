package net.minecraft.src;

public class MMWEntityPig extends EntityPig {
  public MMWEntityPig(World par1World) {
    super(par1World);
  }

  /**
   * Returns the item ID for the item the mob drops on death.
   */
  protected int getDropItemId() {
    return this.isBurning() ? Item.porkCooked.itemID : Item.porkRaw.itemID;
  }

  /**
   * Drop 0-2 items of this living's type
   */
  protected void dropFewItems(boolean par1, int par2) {
    int var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);

    for (int var4 = 0; var4 < var3; ++var4) {
      if (this.isBurning()) {
        this.dropItem(Item.porkCooked.itemID, 1);
      } else {
        this.dropItem(Item.porkRaw.itemID, 1);
      }
    }
    if (this.getSaddled()) {
      this.dropItem(Item.saddle.itemID, 1);
    }
    if (mod_MyWay.peacefulDropBones) {
      this.dropItem(Item.bone.itemID, 1);
    }
  }

  protected void dropRareDrop(int par1) {
    if (mod_MyWay.peacefulAdjustDrops) {
      this.dropItem(Item.leather.itemID, 1);
    }
  }
}
