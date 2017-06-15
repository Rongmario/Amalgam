package amalgam.common.fluid;

import net.minecraft.item.ItemStack;

public abstract interface IAmalgamContainerItem
{
  public abstract AmalgamStack getFluid(ItemStack paramItemStack);
  
  public abstract int fill(ItemStack paramItemStack, AmalgamStack paramAmalgamStack, boolean paramBoolean);
  
  public abstract AmalgamStack drain(ItemStack paramItemStack, int paramInt, boolean paramBoolean);
  
  public abstract int getCapacity(ItemStack paramItemStack);
  
  public abstract int getEmptySpace(ItemStack paramItemStack);
}
