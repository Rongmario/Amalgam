package amalgam.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public final class PacketHandler
{
  public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("amalgam");
  


  public static void init()
  {
    INSTANCE.registerMessage(PacketSyncAmalgamTank.class, PacketSyncAmalgamTank.class, 0, Side.CLIENT);
    INSTANCE.registerMessage(PacketSyncCastingState.class, PacketSyncCastingState.class, 1, Side.CLIENT);
    INSTANCE.registerMessage(PacketSyncCastingItem.class, PacketSyncCastingItem.class, 2, Side.CLIENT);
  }
}
