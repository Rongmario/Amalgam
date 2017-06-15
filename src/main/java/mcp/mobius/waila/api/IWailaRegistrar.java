package mcp.mobius.waila.api;

public abstract interface IWailaRegistrar
{
  public abstract void addConfig(String paramString1, String paramString2, String paramString3);
  
  public abstract void addConfigRemote(String paramString1, String paramString2, String paramString3);
  
  public abstract void addConfig(String paramString1, String paramString2);
  
  public abstract void addConfigRemote(String paramString1, String paramString2);
  
  @Deprecated
  public abstract void registerHeadProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);
  
  @Deprecated
  public abstract void registerBodyProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);
  
  @Deprecated
  public abstract void registerTailProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);
  
  @Deprecated
  public abstract void registerStackProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);
  
  public abstract void registerStackProvider(IWailaDataProvider paramIWailaDataProvider, Class paramClass);
  
  public abstract void registerHeadProvider(IWailaDataProvider paramIWailaDataProvider, Class paramClass);
  
  public abstract void registerBodyProvider(IWailaDataProvider paramIWailaDataProvider, Class paramClass);
  
  public abstract void registerTailProvider(IWailaDataProvider paramIWailaDataProvider, Class paramClass);
  
  public abstract void registerHeadProvider(IWailaEntityProvider paramIWailaEntityProvider, Class paramClass);
  
  public abstract void registerBodyProvider(IWailaEntityProvider paramIWailaEntityProvider, Class paramClass);
  
  public abstract void registerTailProvider(IWailaEntityProvider paramIWailaEntityProvider, Class paramClass);
  
  public abstract void registerOverrideEntityProvider(IWailaEntityProvider paramIWailaEntityProvider, Class paramClass);
  
  public abstract void registerHeadProvider(IWailaFMPProvider paramIWailaFMPProvider, String paramString);
  
  public abstract void registerBodyProvider(IWailaFMPProvider paramIWailaFMPProvider, String paramString);
  
  public abstract void registerTailProvider(IWailaFMPProvider paramIWailaFMPProvider, String paramString);
  
  @Deprecated
  public abstract void registerDecorator(IWailaBlockDecorator paramIWailaBlockDecorator, int paramInt);
  
  public abstract void registerDecorator(IWailaBlockDecorator paramIWailaBlockDecorator, Class paramClass);
  
  public abstract void registerDecorator(IWailaFMPDecorator paramIWailaFMPDecorator, String paramString);
  
  public abstract void registerSyncedNBTKey(String paramString, Class paramClass);
  
  public abstract void registerDocTextFile(String paramString);
  
  public abstract void registerShortDataProvider(IWailaSummaryProvider paramIWailaSummaryProvider, Class paramClass);
}
