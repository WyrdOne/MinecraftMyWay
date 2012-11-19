package net.minecraft.src;

public class MMWEntityCow extends EntityCow {
  public MMWEntityCow(World par1World) {
    super(par1World);
  }

  /**
   * Returns the item ID for the item the mob drops on death.
   */
  protected int getDropItemId() {
    return Item.leather.shiftedIndex;
  }

  /**
   * Drop 0-2 items of this living's type
   */
  protected void dropFewItems(boolean par1, int par2) {
    int var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + par2);
    int var4;

    if (mod_MyWay.peacefulAdjustDrops) {
      var3++;
    }
    for (var4 = 0; var4 < var3; ++var4) {
      this.dropItem(Item.leather.shiftedIndex, 1);
    }

    if (mod_MyWay.peacefulAdjustDrops) {
      var3 = this.rand.nextInt(4) + 2 + this.rand.nextInt(1 + par2);
    } else {
      var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);
    }
    for (var4 = 0; var4 < var3; ++var4) {
      if (this.isBurning()) {
        this.dropItem(Item.beefCooked.shiftedIndex, 1);
      } else {
        this.dropItem(Item.beefRaw.shiftedIndex, 1);
      }
    }
    if (mod_MyWay.peacefulDropBones) {
      this.dropItem(Item.bone.shiftedIndex, 1);
    }
  }
}
