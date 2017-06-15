package amalgam.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryCasting implements net.minecraft.inventory.IInventory
{
  private ItemStack[] stackList;
  private int[] castState;
  private float[] amalgamFillAmount;
  private final int inventoryWidth;
  public ICastingHandler handler;
  
  public InventoryCasting(ICastingHandler handler, int rows, int cols)
  {
    this.inventoryWidth = rows;
    this.stackList = new ItemStack[rows * cols];
    this.castState = new int[rows * cols];
    this.amalgamFillAmount = new float[rows * cols];
    this.handler = handler;
  }
  
  public int getSizeInventory()
  {
    return this.stackList.length;
  }
  
  public ItemStack getStackInSlot(int slot)
  {
    if (this.castState[slot] == 0) {
      return this.stackList[slot];
    }
    
    return null;
  }
  
  public ItemStack decrStackSize(int slot, int decNum)
  {
    if (this.castState[slot] != 0) {
      return null;
    }
    
    if (this.stackList[slot] == null) {
      return null;
    }
    

    if (this.stackList[slot].stackSize <= decNum) {
      ItemStack itemstack = this.stackList[slot];
      setInventorySlotContents(slot, null);
      this.handler.onCastMatrixChanged(this);
      
      return itemstack;
    }
    ItemStack itemstack = this.stackList[slot].splitStack(decNum);
    
    if (this.stackList[slot].stackSize == 0) {
      setInventorySlotContents(slot, itemstack);
    }
    

    this.handler.onCastMatrixChanged(this);
    
    return itemstack;
  }
  


  public ItemStack getStackInSlotOnClosing(int slot)
  {
    return null;
  }
  
  public void setInventorySlotContents(int slot, ItemStack stack)
  {
    if (this.castState[slot] != 0) {
      return;
    }
    
    this.stackList[slot] = stack;
    this.handler.onCastMatrixChanged(this);
  }
  
  public String getInventoryName()
  {
    return "";
  }
  
  public boolean hasCustomInventoryName()
  {
    return false;
  }
  
  public int getInventoryStackLimit()
  {
    return 64;
  }
  
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    return true;
  }
  


  public void openInventory() {}
  

  public void closeInventory() {}
  

  public boolean isItemValidForSlot(int slot, ItemStack stack)
  {
    if (this.castState[slot] == 0) {
      return true;
    }
    
    return false;
  }
  
  public ItemStack getStackInRowAndColumn(int row, int col) {
    if ((row >= 0) && (col < this.inventoryWidth)) {
      int slotNum = row + col * this.inventoryWidth;
      
      return this.stackList[slotNum];
    }
    return null;
  }
  

  public void markDirty() {}
  

  public int getCastState(int slot)
  {
    return this.castState[slot];
  }
  
  public void setCastState(int slot, int state) {
    this.castState[slot] = state;
    this.handler.updateTankCapacity(this);
    this.handler.onCastMatrixChanged(this);
  }
  
  public float getFillAmount(int slot) {
    return this.amalgamFillAmount[slot];
  }
  
  public void setFillAmount(int slot, float amount) {
    this.amalgamFillAmount[slot] = amount;
  }
}
