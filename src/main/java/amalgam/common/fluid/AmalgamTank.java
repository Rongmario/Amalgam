package amalgam.common.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidEvent.FluidDrainingEvent;
import net.minecraftforge.fluids.FluidEvent.FluidFillingEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class AmalgamTank implements IFluidTank
{
  private static final String EMPTY_KEY = "Empty";
  private static final String CAPACITY_KEY = "Capacity";
  protected AmalgamStack fluid;
  protected int capacity;
  protected TileEntity tile;
  
  public AmalgamTank(int cap)
  {
    this.capacity = cap;
  }
  
  public AmalgamTank readFromNBT(NBTTagCompound nbt) {
    if (!nbt.hasKey("Empty")) {
      this.fluid = AmalgamStack.loadAmalgamStackFromNBT(nbt);
    }
    
    this.capacity = nbt.getInteger("Capacity");
    
    return this;
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    if (this.fluid == null) {
      nbt.setString("Empty", "");
    } else {
      this.fluid.writeToNBT(nbt);
    }
    
    nbt.setInteger("Capacity", this.capacity);
    
    return nbt;
  }
  
  public FluidStack getFluid()
  {
    if (this.fluid == null) {
      this.fluid = new AmalgamStack(0, null);
    }
    
    return this.fluid;
  }
  
  public int getFluidAmount()
  {
    if (this.fluid == null) {
      return 0;
    }
    
    return this.fluid.amount;
  }
  
  public int getCapacity()
  {
    return this.capacity;
  }
  
  public AmalgamStack setCapacity(int newCapacity) {
    this.capacity = newCapacity;
    
    if (this.capacity < getFluidAmount()) {
      int extra = this.fluid.amount - this.capacity;
      this.fluid.amount = this.capacity;
      
      return new AmalgamStack(extra, this.fluid.getProperties());
    }
    
    return null;
  }
  
  public FluidTankInfo getInfo()
  {
    return new FluidTankInfo(this);
  }
  
  public int fill(FluidStack resource, boolean doFill)
  {
    if (resource == null) {
      return 0;
    }
    
    if (resource.getFluidID() != amalgam.common.Config.fluidAmalgam.getID()) {
      return 0;
    }
    
    if (doFill) {
      if (this.fluid == null) {
        this.fluid = new AmalgamStack((AmalgamStack)resource, Math.min(this.capacity, resource.amount));
        
        if (this.tile != null) {
          FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(this.fluid, this.tile.getWorldObj(), this.tile.xCoord, this.tile.yCoord, this.tile.zCoord, this, this.fluid.amount));
        }
        

        return this.fluid.amount;
      }
      
      int filled = this.capacity - this.fluid.amount;
      
      if (resource.amount < filled) {
        this.fluid = AmalgamStack.combine(this.fluid, (AmalgamStack)resource);
        filled = resource.amount;
      } else {
        AmalgamStack temp = new AmalgamStack((AmalgamStack)resource, filled);
        this.fluid = AmalgamStack.combine(this.fluid, temp);
      }
      if (this.tile != null) {
        FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(this.fluid, this.tile.getWorldObj(), this.tile.xCoord, this.tile.yCoord, this.tile.zCoord, this, filled));
      }
      

      return filled;
    }
    if (this.fluid == null) {
      return Math.min(this.capacity, resource.amount);
    }
    
    return Math.min(this.capacity - this.fluid.amount, resource.amount);
  }
  

  public FluidStack drain(int maxDrain, boolean doDrain)
  {
    if (this.fluid == null) {
      return null;
    }
    
    int drained = maxDrain;
    
    if ((this.fluid.amount < drained) || (drained == -1)) {
      drained = this.fluid.amount;
    }
    
    AmalgamStack stack = new AmalgamStack(this.fluid, drained);
    
    if (doDrain) {
      this.fluid.amount -= drained;
      
      if (this.fluid.amount <= 0) {
        this.fluid = null;
      }
      
      if (this.tile != null) {
        FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(this.fluid, this.tile.getWorldObj(), this.tile.xCoord, this.tile.yCoord, this.tile.zCoord, this, drained));
      }
    }
    

    return stack;
  }
  
  public String toString()
  {
    if (this.fluid == null) {
      return "Empty!";
    }
    
    return "Capacity: " + getCapacity() + " Amount: " + getFluidAmount() + " Space Left: " + (getCapacity() - getFluidAmount()) + " Properties: " + this.fluid.getProperties().toString();
  }
  
  public void setFluid(AmalgamStack fluid)
  {
    this.fluid = fluid;
  }
}
