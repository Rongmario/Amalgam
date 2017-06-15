package amalgam.common.tile;

import amalgam.common.Config;
import amalgam.common.casting.CastingManager;
import amalgam.common.casting.ICastingRecipe;
import amalgam.common.container.ICastingHandler;
import amalgam.common.container.InventoryCasting;
import amalgam.common.container.InventoryCastingResult;
import amalgam.common.fluid.AmalgamStack;
import amalgam.common.fluid.AmalgamTank;
import amalgam.common.item.ItemAmalgamBlob;
import amalgam.common.properties.PropertyList;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class TileCastingTable
  extends TileAmalgamContainer implements ISidedInventory, ICastingHandler
{
  public InventoryCasting castingInventory;
  public InventoryCastingResult castingResult;
  private static final int RESULT_SLOT = 9;
  private static final String ITEMS_KEY = "ItemStacks";
  private static final String CAST_KEY = "CastStates";
  private static final String STATE_KEY = "State";
  private static final String SLOT_KEY = "Slot";
  
  public TileCastingTable()
  {
    this.tank = new AmalgamTank(0);
    this.castingResult = new InventoryCastingResult();
    this.castingInventory = new InventoryCasting(this, 3, 3);
  }
  
  public void readFromNBT(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
    NBTTagList nbttaglist = tag.getTagList("ItemStacks", 10);
    NBTTagList castList = tag.getTagList("CastStates", 10);
    this.castingInventory = new InventoryCasting(this, 3, 3);
    
    for (int i = 0; i < nbttaglist.tagCount(); i++) {
      NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
      byte slot = nbttagcompound1.getByte("Slot");
      if ((slot >= 0) && (slot < this.castingInventory.getSizeInventory())) {
        this.castingInventory.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(nbttagcompound1));
      }
    }
    
    for (int i = 0; i < castList.tagCount(); i++) {
      NBTTagCompound nbttagcompound1 = castList.getCompoundTagAt(i);
      byte slot = nbttagcompound1.getByte("Slot");
      byte state = nbttagcompound1.getByte("State");
      if ((slot >= 0) && (slot < this.castingInventory.getSizeInventory())) {
        this.castingInventory.setCastState(slot, state);
      }
    }
    
    this.tank.readFromNBT(tag);
    
    updateAmalgamDistribution();
    onCastMatrixChanged(this.castingInventory);
  }
  
  public void writeToNBT(NBTTagCompound tag)
  {
    super.writeToNBT(tag);
    this.tank.writeToNBT(tag);
    
    NBTTagList nbttaglist = new NBTTagList();
    NBTTagList castList = new NBTTagList();
    for (int i = 0; i < this.castingInventory.getSizeInventory(); i++) {
      ItemStack item = this.castingInventory.getStackInSlot(i);
      if (item != null) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setByte("Slot", (byte)i);
        nbttagcompound1.setByte("CastStates", (byte)this.castingInventory.getCastState(i));
        item.writeToNBT(nbttagcompound1);
        nbttaglist.appendTag(nbttagcompound1);
      }
      if (this.castingInventory.getCastState(i) != 0) {
        NBTTagCompound castCompound = new NBTTagCompound();
        castCompound.setByte("Slot", (byte)i);
        castCompound.setByte("State", (byte)this.castingInventory.getCastState(i));
        castList.appendTag(castCompound);
      }
    }
    
    tag.setTag("ItemStacks", nbttaglist);
    tag.setTag("CastStates", castList);
  }
  
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    int fillAmount = super.fill(from, resource, doFill);
    

    if ((fillAmount > 0) && (doFill)) {
      onCastMatrixChanged(this.castingInventory);
    }
    
    updateAmalgamDistribution();
    return fillAmount;
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    FluidStack returnStack = super.drain(from, resource.amount, doDrain);
    
    if ((returnStack != null) && (doDrain)) {
      onCastMatrixChanged(this.castingInventory);
    }
    updateAmalgamDistribution();
    return returnStack;
  }
  
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    FluidStack returnStack = super.drain(from, maxDrain, doDrain);
    
    if ((returnStack != null) && (doDrain)) {
      onCastMatrixChanged(this.castingInventory);
    }
    updateAmalgamDistribution();
    return returnStack;
  }
  
  public void updateTankCapacity(InventoryCasting inv) {
    int newCapacity = 0;
    

    for (int i = 0; i < 9; i++) {
      switch (inv.getCastState(i)) {
      case 1: 
        newCapacity++;
        break;
      case 2: 
        newCapacity += 9;
        break;
      case 3: 
        newCapacity += 81;
      }
      
    }
    


    AmalgamStack extraAmalgam = this.tank.setCapacity(newCapacity);
    
    if (extraAmalgam != null) {
      if (extraAmalgam.amount == 0) {
        return;
      }
      

      if (!this.worldObj.isRemote)
      {
        ItemStack droppedBlob = new ItemStack(Config.amalgamBlob, 1);
        ((ItemAmalgamBlob)Config.amalgamBlob).setProperties(droppedBlob, extraAmalgam.getProperties());
        ((ItemAmalgamBlob)Config.amalgamBlob).setVolume(droppedBlob, extraAmalgam.amount);
        EntityItem amalgEntity = new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, droppedBlob);
        this.worldObj.spawnEntityInWorld(amalgEntity);
      }
    }
    
    updateAmalgamDistribution();
  }
  
  public Packet getDescriptionPacket()
  {
    NBTTagCompound tag = new NBTTagCompound();
    writeToNBT(tag);
    
    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
  }
  
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
  {
    NBTTagCompound tag = pkt.func_148857_g();
    readFromNBT(tag);
  }
  
  public long getFluidAmount() {
    return this.tank.getFluidAmount();
  }
  
  public void setTankFluid(AmalgamStack fluid) {
    this.tank.setFluid(fluid);
    updateAmalgamDistribution();
  }
  
  public int getSizeInventory()
  {
    return this.castingInventory.getSizeInventory();
  }
  
  public ItemStack getStackInSlotOnClosing(int slot)
  {
    if (slot == 9) {
      return this.castingResult.getStackInSlotOnClosing(0);
    }
    return this.castingInventory.getStackInSlotOnClosing(slot);
  }
  
  public void setInventorySlotContents(int slot, ItemStack stack)
  {
    if (slot == 9) {
      this.castingResult.setInventorySlotContents(0, stack);
    }
    this.castingInventory.setInventorySlotContents(slot, stack);
  }
  
  public String getInventoryName()
  {
    return "container.castingtable";
  }
  
  public boolean hasCustomInventoryName()
  {
    return true;
  }
  
  public int getInventoryStackLimit()
  {
    return this.castingInventory.getInventoryStackLimit();
  }
  
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    return this.castingInventory.isUseableByPlayer(player);
  }
  


  public void openInventory() {}
  

  public void closeInventory() {}
  

  public boolean isItemValidForSlot(int slot, ItemStack stack)
  {
    if (slot == 9) {
      return this.castingResult.isItemValidForSlot(0, stack);
    }
    
    return this.castingInventory.isItemValidForSlot(slot, stack);
  }
  
  public ItemStack decrStackSize(int slot, int decNum)
  {
    if (slot == 9) {
      return this.castingResult.decrStackSize(0, decNum);
    }
    
    return this.castingInventory.decrStackSize(slot, decNum);
  }
  
  public int[] getAccessibleSlotsFromSide(int side)
  {
    return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  }
  
  public boolean canInsertItem(int slot, ItemStack stack, int meta)
  {
    if (slot == 9) {
      return false;
    }
    
    return true;
  }
  
  public boolean canExtractItem(int slot, ItemStack stack, int meta)
  {
    if ((slot == 9) && (getEmptySpace() == 0)) {
      return true;
    }
    
    return false;
  }
  
  public ItemStack getStackInSlot(int slot)
  {
    if (slot == 9) {
      return this.castingResult.getStackInSlot(0);
    }
    
    return this.castingInventory.getStackInSlot(slot);
  }
  
  public boolean isCastComplete()
  {
    return this.tank.getCapacity() == this.tank.getFluidAmount();
  }
  
  public void onCastMatrixChanged(InventoryCasting inv)
  {
    ICastingRecipe recipe = CastingManager.findMatchingRecipe(inv, this.worldObj);
    
    if (recipe == null) {
      this.castingResult.setInventorySlotContents(0, null);
      return;
    }
    
    PropertyList pList = getAmalgamPropertyList();
    if (getFluidAmount() == 0) {
      pList = null;
    }
    
    this.castingResult.setInventorySlotContents(0, recipe.getCastingResult(inv, pList));
  }
  
  public void onCastPickup()
  {
    this.tank.setFluid(null);
    
    onCastMatrixChanged(this.castingInventory);
    updateAmalgamDistribution();
  }
  
  public void updateAmalgamDistribution() {
	  if(getFluidAmount() > 0) {
		  long fillPercentage = getFluidAmount() / getTankCapacity();
		  for (int i = 0; i < this.castingInventory.getSizeInventory(); i++) {
			  if (this.castingInventory.getCastState(i) != 0) {
				  this.castingInventory.setFillAmount(i, fillPercentage);
			  }
	      } 
	  } 
  }
}
