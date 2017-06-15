package amalgam.common.tile;

import amalgam.common.Config;
import amalgam.common.fluid.AmalgamStack;
import amalgam.common.fluid.AmalgamTank;
import amalgam.common.item.ItemAmalgamBlob;
import amalgam.common.network.PacketHandler;
import amalgam.common.network.PacketSyncAmalgamTank;
import amalgam.common.properties.PropertyList;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public abstract class TileAmalgamContainer extends net.minecraft.tileentity.TileEntity implements net.minecraftforge.fluids.IFluidHandler
{
  public AmalgamTank tank;
  
  public FluidTankInfo[] getTankInfo(ForgeDirection from)
  {
    return new FluidTankInfo[] { this.tank.getInfo() };
  }
  
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    int returnValue = this.tank.fill(resource, doFill);
    if ((doFill) && (returnValue > 0) && (!this.worldObj.isRemote))
    {


      PacketHandler.INSTANCE.sendToAll(new PacketSyncAmalgamTank((AmalgamStack)getTankInfo(null)[0].fluid, this.xCoord, this.yCoord, this.zCoord));
    }
    
    return returnValue;
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    if (resource == null) {
      return null;
    }
    FluidStack returnStack = this.tank.drain(resource.amount, doDrain);
    if ((doDrain) && (returnStack != null) && (!this.worldObj.isRemote)) {
      PacketHandler.INSTANCE.sendToAll(new PacketSyncAmalgamTank((AmalgamStack)getTankInfo(null)[0].fluid, this.xCoord, this.yCoord, this.zCoord));
    }
    
    return returnStack;
  }
  
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    FluidStack returnStack = this.tank.drain(maxDrain, doDrain);
    if ((doDrain) && (returnStack != null) && (!this.worldObj.isRemote)) {
      PacketHandler.INSTANCE.sendToAll(new PacketSyncAmalgamTank((AmalgamStack)getTankInfo(null)[0].fluid, this.xCoord, this.yCoord, this.zCoord));
    }
    
    return returnStack;
  }
  
  public boolean canFill(ForgeDirection from, Fluid fluid)
  {
    if (fluid.getID() == Config.fluidAmalgam.getID()) {
      return true;
    }
    
    return false;
  }
  
  public boolean canDrain(ForgeDirection from, Fluid fluid)
  {
    return true;
  }
  
  public void emptyTank() {
    if (!this.worldObj.isRemote) {
      int amount = this.tank.getFluidAmount();
      PropertyList pList = ((AmalgamStack)this.tank.getFluid()).getProperties();
      
      while (amount > 0) {
        int dropAmount = Math.min(amount, 9);
        amount -= dropAmount;
        ItemStack droppedBlob = new ItemStack(Config.amalgamBlob, 1);
        
        ((ItemAmalgamBlob)Config.amalgamBlob).setProperties(droppedBlob, pList);
        ((ItemAmalgamBlob)Config.amalgamBlob).setVolume(droppedBlob, dropAmount);
        
        EntityItem amalgEntity = new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, droppedBlob);
        this.worldObj.spawnEntityInWorld(amalgEntity);
      }
    }
  }
  
  public PropertyList getAmalgamPropertyList() {
    return ((AmalgamStack)this.tank.getFluid()).getProperties();
  }
  
  public void setTankFluid(AmalgamStack fluid) {
    this.tank.setFluid(fluid);
  }
  
  public int getEmptySpace() {
    return this.tank.getCapacity() - this.tank.getFluidAmount();
  }
  
  public int getFluidVolume() {
    return this.tank.getFluidAmount();
  }
  
  public long getTankCapacity() {
    return this.tank.getCapacity();
  }
  
  public AmalgamStack getAmalgamStack()
  {
    return (AmalgamStack)this.tank.getFluid();
  }
}
