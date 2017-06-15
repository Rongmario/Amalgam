package mcp.mobius.waila.api;

import java.util.List;
import net.minecraft.item.ItemStack;

@Deprecated
public abstract interface IWailaBlock
{
  public abstract ItemStack getWailaStack(IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler);
  
  public abstract List<String> getWailaHead(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler);
  
  public abstract List<String> getWailaBody(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler);
  
  public abstract List<String> getWailaTail(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler);
}
