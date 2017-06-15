package amalgam.common;

import amalgam.common.fluid.AmalgamStack;
import amalgam.common.fluid.IAmalgamContainerItem;
import amalgam.common.properties.Property;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import amalgam.common.tile.TileAmalgamContainer;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WailaProvider implements IWailaDataProvider
{
  public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
  {
    return null;
  }
  
  public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
  {
    return currenttip;
  }
  
  public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
  {
    net.minecraft.tileentity.TileEntity te = accessor.getTileEntity();
    if ((te instanceof TileAmalgamContainer)) {
      AmalgamStack amalgStack = ((TileAmalgamContainer)te).getAmalgamStack();
      long capacity = ((TileAmalgamContainer)te).getTankCapacity();
      currenttip.add("Volume: " + amalgStack.amount + "/" + capacity);
      
      if ((amalgStack.amount != 0) && (accessor.getPlayer().isSneaking())) {
        currenttip.add("--------");
        addPropertiesToTooltip(currenttip, amalgStack, accessor.getPlayer());
      }
    }
    
    return currenttip;
  }
  
  public void addPropertiesToTooltip(List<String> currentTip, AmalgamStack amalgStack, EntityPlayer player) {
    ItemStack currentItem = player.inventory.getCurrentItem();
    
    if ((currentItem != null) && (PropertyManager.itemIsAmalgable(currentItem))) {
      AmalgamStack a = new AmalgamStack(PropertyManager.getVolume(currentItem), PropertyManager.getProperties(currentItem));
      a = AmalgamStack.combine(a, amalgStack);
      
      for (Property p : Property.getAll()) {
        if (p != PropertyManager.COLOR)
        {


          float currentValue = amalgStack.getProperties().getValue(p);
          float newValue = a.getProperties().getValue(p);
          currentTip.add(p.getName() + ": " + SpecialChars.WHITE + String.format("%.1f", new Object[] { Float.valueOf(currentValue) }) + "->" + (currentValue > newValue ? SpecialChars.RED : SpecialChars.GREEN) + String.format("%.1f", new Object[] { Float.valueOf(newValue) }));
        }
      }
    }
    else if ((currentItem != null) && ((currentItem.getItem() instanceof IAmalgamContainerItem))) {
      AmalgamStack a = ((IAmalgamContainerItem)currentItem.getItem()).getFluid(currentItem);
      a = AmalgamStack.combine(a, amalgStack);
      
      for (Property p : Property.getAll()) {
        if (p != PropertyManager.COLOR)
        {


          float currentValue = amalgStack.getProperties().getValue(p);
          float newValue = a.getProperties().getValue(p);
          currentTip.add(p.getName() + ": " + SpecialChars.WHITE + String.format("%.1f", new Object[] { Float.valueOf(currentValue) }) + "->" + (currentValue > newValue ? SpecialChars.RED : SpecialChars.GREEN) + String.format("%.1f", new Object[] { Float.valueOf(newValue) }));
        }
      }
    } else {
      for (Property p : Property.getAll()) {
        if (p != PropertyManager.COLOR)
        {


          currentTip.add(p.getName() + ": " + SpecialChars.WHITE + String.format("%.1f", new Object[] { Float.valueOf(amalgStack.getProperties().getValue(p)) }));
        }
      }
    }
  }
  
  public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
    return currenttip;
  }
  
  public static void callbackRegister(IWailaRegistrar registrar) {
    registrar.registerBodyProvider(new WailaProvider(), Block.class);
  }

@Override
public NBTTagCompound getNBTData(EntityPlayerMP arg0, TileEntity arg1, NBTTagCompound arg2, World arg3, int arg4,
		int arg5, int arg6) {
	// TODO Auto-generated method stub
	return null;
}
}
