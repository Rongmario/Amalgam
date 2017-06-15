package mcp.mobius.waila.api;

import net.minecraft.item.ItemStack;

public abstract interface IWailaFMPDecorator
{
  public abstract void decorateBlock(ItemStack paramItemStack, IWailaFMPAccessor paramIWailaFMPAccessor, IWailaConfigHandler paramIWailaConfigHandler);
}
