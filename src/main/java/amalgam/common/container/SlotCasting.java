package amalgam.common.container;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCasting extends Slot
{
  private static final int MAX_STATE = 3;
  public static final int EMPTY_STATE = 0;
  public static final int NUGGET_STATE = 1;
  public static final int INGOT_STATE = 2;
  public static final int BLOCK_STATE = 3;
  
  public SlotCasting(InventoryCasting inv, int slotNum, int xPos, int yPos)
  {
    super(inv, slotNum, xPos, yPos);
  }
  
  public boolean isItemValid(ItemStack stack) {
    return getCastState() == 0;
  }
  
  public int toggleCastState(boolean up) {
    int castState = getCastState();
    if (up) {
      castState += 1;
    } else {
      castState -= 1;
    }
    
    if (castState > 3) {
      castState = 0;
    } else if (castState < 0) {
      castState = 3;
    }
    
    setCastState(castState);
    return castState;
  }
  
  public void setCastState(int castState) {
    ((InventoryCasting)this.inventory).setCastState(this.slotNumber, castState);
  }
  
  public int getCastState()
  {
    return ((InventoryCasting)this.inventory).getCastState(this.slotNumber);
  }
  
  public float getFillLevel() {
    return ((InventoryCasting)this.inventory).getFillAmount(this.slotNumber);
  }
}
