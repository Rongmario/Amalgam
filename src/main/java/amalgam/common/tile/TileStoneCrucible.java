package amalgam.common.tile;

import amalgam.common.Config;
import amalgam.common.fluid.AmalgamStack;
import amalgam.common.fluid.AmalgamTank;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.Logger;

public class TileStoneCrucible
  extends TileAmalgamContainer
  implements IInventory
{
  private static Set<Block> heatSources = Sets.newHashSet(new Block[] { Blocks.fire, Blocks.lava, Blocks.flowing_lava });
  
  private static final String HEAT_TAG = "heat";
  private boolean hasHeat;
  private static final int UPDATE_PERIOD = 50;
  private int ticksSinceUpdate;
  
  public TileStoneCrucible()
  {
    this.tank = new AmalgamTank(135);
  }
  
  public void readFromNBT(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
    this.tank.readFromNBT(tag);
    this.hasHeat = tag.getBoolean("heat");
  }
  
  public void writeToNBT(NBTTagCompound tag)
  {
    super.writeToNBT(tag);
    this.tank.writeToNBT(tag);
    tag.setBoolean("heat", this.hasHeat);
  }
  
  public void updateEntity()
  {
    this.ticksSinceUpdate += 1;
    
    if (this.ticksSinceUpdate > 50) {
      this.ticksSinceUpdate = 0;
      Block test = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord);
      
      if (heatSources.contains(test)) {
        this.hasHeat = true;
      } else {
        this.hasHeat = false;
      }
    }
  }
  
  public static void addHeatSource(Block source) {
    heatSources.add(source);
  }
  
  public boolean isHot() {
    return this.hasHeat;
  }
  
  public float getRenderLiquidLevel() {
    return 0.3F + 0.69F * (this.tank.getFluidAmount() / this.tank.getCapacity());
  }
  
  public Packet getDescriptionPacket()
  {
    NBTTagCompound tag = new NBTTagCompound();
    this.tank.writeToNBT(tag);
    tag.setBoolean("heat", this.hasHeat);
    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
  }
  
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
  {
    NBTTagCompound tag = pkt.func_148857_g();
    this.tank.readFromNBT(tag);
    this.hasHeat = tag.getBoolean("heat");
  }
  
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getRenderBoundingBox()
  {
    return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
  }
  
  public int getSizeInventory()
  {
    return 1;
  }
  
  public ItemStack getStackInSlot(int slot)
  {
    return null;
  }
  
  public ItemStack decrStackSize(int slot, int amount)
  {
    return null;
  }
  
  public ItemStack getStackInSlotOnClosing(int slot)
  {
    return null;
  }
  
  public void setInventorySlotContents(int slot, ItemStack stack)
  {
    if ((slot == 0) && (stack != null) && (stack.stackSize == 1)) {
      if (!isHot()) {
        return;
      }
      
      int amount = PropertyManager.getVolume(stack);
      
      if ((amount > 0) && (amount <= getEmptySpace())) {
        PropertyList amalgProperties = PropertyManager.getProperties(stack);
        AmalgamStack amalg = new AmalgamStack(amount, amalgProperties);
        
        if (amalgProperties == null) {
          Config.LOG.error("No properties!!!!!");
        }
        
        fill(ForgeDirection.UNKNOWN, amalg, true);
      }
    } else {
      Config.LOG.error("Error while inputting item into crucible");
    }
  }
  
  public String getInventoryName()
  {
    return null;
  }
  
  public boolean hasCustomInventoryName()
  {
    return false;
  }
  
  public int getInventoryStackLimit()
  {
    return 1;
  }
  
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    return false;
  }
  


  public void openInventory() {}
  

  public void closeInventory() {}
  

  public boolean isItemValidForSlot(int slot, ItemStack stack)
  {
    if ((stack != null) && (PropertyManager.itemIsAmalgable(stack)) && (PropertyManager.getVolume(stack) <= getEmptySpace())) {
      return true;
    }
    return false;
  }
}
