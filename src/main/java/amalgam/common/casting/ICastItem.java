package amalgam.common.casting;

import amalgam.common.properties.PropertyList;
import net.minecraft.item.ItemStack;

public abstract interface ICastItem
{
  public abstract ItemStack generateStackWithProperties(PropertyList paramPropertyList, ItemStack[] paramArrayOfItemStack, int paramInt);
}
