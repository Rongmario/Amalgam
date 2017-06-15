package amalgam.common.fluid;

import amalgam.common.properties.PropertyList;
import net.minecraft.item.ItemStack;

public abstract interface IAmalgableItem
{
  public abstract int getVolume(ItemStack paramItemStack);
  
  public abstract PropertyList getProperties(ItemStack paramItemStack);
}
