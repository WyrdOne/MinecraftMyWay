package net.minecraft.src;

import java.util.Random;

public class MMWEntitySheep extends EntitySheep {
  public MMWEntitySheep(World par1World) {
    super(par1World);
  }

  /**
   * Returns the item ID for the item the mob drops on death.
   */
  protected int getDropItemId() {
      return Block.cloth.blockID;
  }

  /**
   * Drop 0-2 items of this living's type
   */
  protected void dropFewItems(boolean par1, int par2) {
    if (!this.getSheared()) {
      this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 0.0F);
    }
    if (mod_MyWay.peacefulAdjustDrops) {
      int var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);

      for (int var4 = 0; var4 < var3; ++var4) {
        if (this.isBurning()) {
          this.dropItem(Item.porkCooked.shiftedIndex, 1);
        } else {
          this.dropItem(Item.porkRaw.shiftedIndex, 1);
        }
      }
    }
    if (mod_MyWay.peacefulDropBones) {
      this.dropItem(Item.bone.shiftedIndex, 1);
    }
  }
}
