package mcp.mobius.waila.api;

import java.util.LinkedHashMap;
import net.minecraft.item.ItemStack;

public abstract interface IWailaSummaryProvider
{
  public abstract LinkedHashMap<String, String> getSummary(ItemStack paramItemStack, LinkedHashMap<String, String> paramLinkedHashMap, IWailaConfigHandler paramIWailaConfigHandler);
}
