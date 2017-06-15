package amalgam.common.block;

import amalgam.common.fluid.AmalgamStack;
import amalgam.common.fluid.IAmalgamContainerItem;
import amalgam.common.properties.PropertyManager;
import amalgam.common.tile.TileAmalgamContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockAmalgamContainer extends Block implements net.minecraft.block.ITileEntityProvider
{
  protected BlockAmalgamContainer(Material mat)
  {
    super(mat);
  }
  
  public void breakBlock(World world, int x, int y, int z, Block block, int metaData)
  {
    TileEntity te = world.getTileEntity(x, y, z);
    if ((te instanceof TileAmalgamContainer)) {
      ((TileAmalgamContainer)te).emptyTank();
    }
    
    super.breakBlock(world, x, y, z, block, metaData);
    world.removeTileEntity(x, y, z);
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
  {
    ItemStack stack = player.inventory.getCurrentItem();
    
    if (stack == null) {
      return true;
    }
    
    TileEntity te = world.getTileEntity(x, y, z);
    
    if (PropertyManager.itemIsAmalgable(stack)) {
      interactWithAmalgableItem(te, stack);
      return true;
    }
    
    Item stackItem = stack.getItem();
    
    if ((stackItem instanceof IAmalgamContainerItem)) {
      interactWithAmalgamContainerItem(te, (IAmalgamContainerItem)stackItem, stack, player);
      return true;
    }
    
    return false;
  }
  
  protected abstract void interactWithAmalgableItem(TileEntity paramTileEntity, ItemStack paramItemStack);
  
  protected void interactWithAmalgamContainerItem(TileEntity te, IAmalgamContainerItem container, ItemStack stack, EntityPlayer player) {
    if (!(te instanceof TileAmalgamContainer)) {
      return;
    }
    
    TileAmalgamContainer handler = (TileAmalgamContainer)te;
    if (player.isSneaking()) {
      int drainAmount = Math.min(container.getEmptySpace(stack), 1);
      AmalgamStack fluidStack = (AmalgamStack)handler.drain(ForgeDirection.UNKNOWN, drainAmount, true);
      
      if (fluidStack != null) {
        int result = container.fill(stack, fluidStack, true);
        fluidStack.amount -= result;
        
        if (fluidStack.amount > 0) {
          handler.fill(ForgeDirection.UNKNOWN, fluidStack, true);
        }
      }
    } else if (container.getFluid(stack).amount == 0) {
      AmalgamStack fluidStack = (AmalgamStack)handler.drain(ForgeDirection.UNKNOWN, container.getEmptySpace(stack), true);
      
      if (fluidStack != null) {
        int result = container.fill(stack, fluidStack, true);
        fluidStack.amount -= result;
        
        if (fluidStack.amount > 0) {
          handler.fill(ForgeDirection.UNKNOWN, fluidStack, true);
        }
      }
    } else {
      AmalgamStack newStack = container.drain(stack, container.getCapacity(stack), true);
      int filled = handler.fill(ForgeDirection.UNKNOWN, newStack, true);
      newStack.amount -= filled;
      container.fill(stack, newStack, true);
    }
  }
  




  public boolean hasComparatorInputOverride()
  {
    return true;
  }
  




  public int getComparatorInputOverride(World world, int x, int y, int z, int meta)
  {
    TileEntity amalgContainer = world.getTileEntity(x, y, z);
    if ((amalgContainer instanceof TileAmalgamContainer)) {
      return (int)(15.0F * ((TileAmalgamContainer)amalgContainer).getFluidVolume() / ((TileAmalgamContainer)amalgContainer).getTankCapacity());
    }
    

    return 0;
  }
}
