package amalgam.common.network;

import amalgam.common.Amalgam;
import amalgam.common.CommonProxy;
import amalgam.common.tile.TileCastingTable;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketSyncCastingItem implements IMessage, cpw.mods.fml.common.network.simpleimpl.IMessageHandler<PacketSyncCastingItem, IMessage>
{
  private ItemStack itemStack;
  private int slot;
  private int x;
  private int y;
  private int z;
  
  public PacketSyncCastingItem() {}
  
  public PacketSyncCastingItem(ItemStack itemStack, int slot, int x, int y, int z)
  {
    this.itemStack = itemStack;
    this.slot = slot;
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
    
    PacketBuffer pb = new PacketBuffer(buf);
    try {
      this.itemStack = pb.readItemStackFromBuffer();
    }
    catch (IOException e) {}
  }
  
  public void toBytes(ByteBuf buf)
  {
    buf.writeInt(this.x);
    buf.writeInt(this.y);
    buf.writeInt(this.z);
    
    buf.writeByte(this.slot);
    
    PacketBuffer pb = new PacketBuffer(buf);
    try {
      pb.writeItemStackToBuffer(this.itemStack);
    }
    catch (IOException e) {}
  }
  

  public IMessage onMessage(PacketSyncCastingItem message, MessageContext ctx)
  {
    TileEntity te = Amalgam.proxy.getClientWorld().getTileEntity(message.x, message.y, message.z);
    
    if ((te instanceof TileCastingTable))
    {

      ((TileCastingTable)te).setInventorySlotContents(message.slot, message.itemStack);
    }
    
    return null;
  }
}
