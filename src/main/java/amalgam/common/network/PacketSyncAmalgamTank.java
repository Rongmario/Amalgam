package amalgam.common.network;

import amalgam.common.Amalgam;
import amalgam.common.CommonProxy;
import amalgam.common.fluid.AmalgamStack;
import amalgam.common.tile.TileAmalgamContainer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;

public class PacketSyncAmalgamTank implements IMessage, cpw.mods.fml.common.network.simpleimpl.IMessageHandler<PacketSyncAmalgamTank, IMessage>
{
  private AmalgamStack amalgamStack;
  private int x;
  private int y;
  private int z;
  
  public PacketSyncAmalgamTank() {}
  
  public PacketSyncAmalgamTank(AmalgamStack amalgamStack, int x, int y, int z)
  {
    this.amalgamStack = amalgamStack;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public void fromBytes(ByteBuf buf)
  {
    this.x = buf.readInt();
    this.y = buf.readInt();
    this.z = buf.readInt();
    
    PacketBuffer pb = new PacketBuffer(buf);
    try {
      this.amalgamStack = AmalgamStack.loadAmalgamStackFromNBT(pb.readNBTTagCompoundFromBuffer());
    }
    catch (IOException e) {}
  }
  
  public void toBytes(ByteBuf buf)
  {
    buf.writeInt(this.x);
    buf.writeInt(this.y);
    buf.writeInt(this.z);
    
    PacketBuffer pb = new PacketBuffer(buf);
    try {
      pb.writeNBTTagCompoundToBuffer(this.amalgamStack.writeToNBT(new NBTTagCompound()));
    }
    catch (IOException e) {}
  }
  
  public IMessage onMessage(PacketSyncAmalgamTank message, MessageContext ctx)
  {
    net.minecraft.tileentity.TileEntity te = Amalgam.proxy.getClientWorld().getTileEntity(message.x, message.y, message.z);
    
    if ((te instanceof TileAmalgamContainer))
    {

      ((TileAmalgamContainer)te).setTankFluid(message.amalgamStack);
    }
    
    return null;
  }
}
