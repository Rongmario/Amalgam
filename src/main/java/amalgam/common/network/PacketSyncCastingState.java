package amalgam.common.network;

import amalgam.common.tile.TileCastingTable;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.Logger;

public class PacketSyncCastingState implements IMessage, cpw.mods.fml.common.network.simpleimpl.IMessageHandler<PacketSyncCastingState, IMessage>
{
  private int slot;
  private int state;
  private int x;
  private int y;
  private int z;
  
  public PacketSyncCastingState() {}
  
  public PacketSyncCastingState(int slotNum, int slotState, int x, int y, int z)
  {
    this.slot = slotNum;
    this.state = slotState;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public void fromBytes(ByteBuf buf)
  {
    this.x = buf.readInt();
    this.y = buf.readInt();
    this.z = buf.readInt();
    
    this.slot = buf.readByte();
    this.state = buf.readByte();
  }
  
  public void toBytes(ByteBuf buf)
  {
    buf.writeInt(this.x);
    buf.writeInt(this.y);
    buf.writeInt(this.z);
    
    buf.writeByte(this.slot);
    buf.writeByte(this.state);
  }
  
  public IMessage onMessage(PacketSyncCastingState message, MessageContext ctx)
  {
    net.minecraft.tileentity.TileEntity te = amalgam.common.Amalgam.proxy.getClientWorld().getTileEntity(message.x, message.y, message.z);
    
    if ((te instanceof TileCastingTable)) {
      amalgam.common.Config.LOG.info("Seeting slot " + message.slot + " to state " + message.state);
      ((TileCastingTable)te).castingInventory.setCastState(message.slot, message.state);
    }
    
    return null;
  }
}
