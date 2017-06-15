package mcp.mobius.waila.api;

import java.util.List;
import net.minecraft.item.ItemStack;

public abstract interface IWailaFMPProvider
{
  public abstract List<String> getWailaHead(ItemStack paramItemStack, List<String> paramList, IWailaFMPAccessor paramIWailaFMPAccessor, IWailaConfigHandler paramIWailaConfigHandler);
  
  public abstract List<String> getWailaBody(ItemStack paramItemStack, List<String> paramList, IWailaFMPAccessor paramIWailaFMPAccessor, IWailaConfigHandler paramIWailaConfigHandler);
  
  public abstract List<String> getWailaTail(ItemStack paramItemStack, List<String> paramList, IWailaFMPAccessor paramIWailaFMPAccessor, IWailaConfigHandler paramIWailaConfigHandler);
}
