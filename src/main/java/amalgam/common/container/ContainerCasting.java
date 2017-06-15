package amalgam.common.container;

import amalgam.common.Config;
import amalgam.common.casting.ICastingRecipe;
import amalgam.common.network.PacketHandler;
import amalgam.common.network.PacketSyncCastingItem;
import amalgam.common.network.PacketSyncCastingState;
import amalgam.common.tile.TileCastingTable;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

public class ContainerCasting extends Container
{
  public TileCastingTable castingTable;
  
  public ContainerCasting(InventoryPlayer inv, TileCastingTable te)
  {
    this.castingTable = te;

    for (int rowNum = 0; rowNum < 3; rowNum++) {
      for (int colNum = 0; colNum < 3; colNum++) {
        int slotNum = colNum + rowNum * 3;
        
        SlotCasting s = new SlotCasting(this.castingTable.castingInventory, slotNum, 30 + colNum * 18, 17 + rowNum * 18);
        addSlotToContainer(s);
      }
    }
    
    addSlotToContainer(new SlotCastingResult(inv.player, this.castingTable.castingInventory, this.castingTable.castingResult, 0, 124, 35));
    
    for (windowId = 0; windowId < 3; windowId++) {
      for (int colNum = 0; colNum < 9; colNum++) {
        int slotNum = colNum + windowId * 9 + 9;
        addSlotToContainer(new Slot(inv, slotNum, 8 + colNum * 18, 84 + windowId * 18));
      }
    }
    
    for (int colNum = 0; colNum < 9; colNum++) {
      addSlotToContainer(new Slot(inv, colNum, 8 + colNum * 18, 142));
    }
    
    onCraftMatrixChanged(this.castingTable.castingInventory);
  }
  
  public boolean canInteractWith(EntityPlayer player)
  {
    return player.getDistanceSq(this.castingTable.xCoord + 0.5D, this.castingTable.yCoord + 0.5D, this.castingTable.zCoord + 0.5D) <= 64.0D;
  }
  


  public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
  {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.inventorySlots.get(slotNum);
    
    if ((slot != null) && (slot.getHasStack())) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      
      if (slotNum == 0) {
        if (!mergeItemStack(itemstack1, 10, 46, true)) {
          return null;
        }
        
        slot.onSlotChange(itemstack1, itemstack);
      } else if ((slotNum >= 10) && (slotNum < 37)) {
        if (!mergeItemStack(itemstack1, 37, 46, false)) {
          return null;
        }
      } else if ((slotNum >= 37) && (slotNum < 46)) {
        if (!mergeItemStack(itemstack1, 10, 37, false)) {
          return null;
        }
      } else if (!mergeItemStack(itemstack1, 10, 46, false)) {
        return null;
      }
      
      if (itemstack1.stackSize == 0) {
        slot.putStack((ItemStack)null);
      } else {
        slot.onSlotChanged();
      }
      
      if (itemstack1.stackSize == itemstack.stackSize) {
        return null;
      }
      
      slot.onPickupFromSlot(player, itemstack1);
    }
    
    return itemstack;
  }
  
  public void onCraftMatrixChanged(IInventory inv)
  {
    super.onCraftMatrixChanged(inv);
    
    ICastingRecipe recipe = amalgam.common.casting.CastingManager.findMatchingRecipe(this.castingTable.castingInventory, this.castingTable.getWorldObj());
    
    if (recipe == null) {
      this.castingTable.castingResult.setInventorySlotContents(0, null);
      return;
    }
    
    amalgam.common.properties.PropertyList pList = this.castingTable.getAmalgamPropertyList();
    if (this.castingTable.getFluidAmount() == 0) {
      pList = null;
    }
    
    this.castingTable.castingResult.setInventorySlotContents(0, recipe.getCastingResult(this.castingTable.castingInventory, pList));
  }
  

  public ItemStack slotClick(int slotNum, int ctrNum, int shiftNum, EntityPlayer player)
  {
    if ((!this.castingTable.getWorldObj().isRemote) && 
      (slotNum >= 0) && (slotNum < this.inventorySlots.size()) && (shiftNum != 6)) {
      Slot slot = getSlot(slotNum);
      
      if (((slot instanceof SlotCasting)) && 
        (!slot.getHasStack()) && (player.inventory.getItemStack() == null))
      {
        int castState = this.castingTable.castingInventory.getCastState(slotNum);
        if (shiftNum == 0) {
          castState += 1;
        } else {
          castState -= 1;
        }
        
        if (castState > 3) {
          castState = 0;
        } else if (castState < 0) {
          castState = 3;
        }
        

        this.castingTable.castingInventory.setCastState(slot.slotNumber, castState);
        
        PacketHandler.INSTANCE.sendToAll(new PacketSyncCastingState(slot.slotNumber, castState, this.castingTable.xCoord, this.castingTable.yCoord, this.castingTable.zCoord));
      }
    }
    



    return super.slotClick(slotNum, ctrNum, shiftNum, player);
  }
  
  public void detectAndSendChanges()
  {
    for (int i = 0; i < this.inventorySlots.size(); i++) {
      ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
      ItemStack itemstack1 = (ItemStack)this.inventoryItemStacks.get(i);
      
      if ((!ItemStack.areItemStacksEqual(itemstack1, itemstack)) && ((this.inventorySlots.get(i) instanceof SlotCasting))) {
        Config.LOG.info("Change detected");
        itemstack1 = itemstack == null ? null : itemstack.copy();
        this.inventoryItemStacks.set(i, itemstack1);
        
        PacketHandler.INSTANCE.sendToAll(new PacketSyncCastingItem(itemstack1, i, this.castingTable.xCoord, this.castingTable.yCoord, this.castingTable.zCoord));
      }
    }
  }
}
