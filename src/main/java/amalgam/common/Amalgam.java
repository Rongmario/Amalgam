package amalgam.common;

import amalgam.common.network.PacketHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid=Amalgam.MODID, version=Amalgam.VERSION, guiFactory="amalgam.client.gui.AmalgamGuiFactory")
public class Amalgam
{
  public static final String MODID = "Amalgam";
  public static final String VERSION = "0.5.0";
  
  @Mod.Instance("Amalgam")
  public static Amalgam instance;
  
  @SidedProxy(clientSide="amalgam.client.ClientProxy", serverSide="amalgam.common.CommonProxy")
  public static CommonProxy proxy;
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    Config.init(event);
    
    Config.registerItems();
    Config.registerBlocks();
    Config.registerFluids();
    
    Config.registerEntities();
    PacketHandler.init();
  }
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
	  
    NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
    FMLCommonHandler.instance().bus().register(Config.instance);
    Config.registerAmalgamProperties();
    Config.registerRecipes();
    
    FMLInterModComms.sendMessage("Waila", "register", "amalgam.common.WailaProvider.callbackRegister");
  }
  
  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    proxy.registerRenderers();
    
    Config.removeVanillaRecipes();
  }
}
