package amalgam.common.item;

import amalgam.common.Config;
import amalgam.common.fluid.AmalgamStack;
import amalgam.common.fluid.IAmalgamContainerItem;
import amalgam.common.properties.Property;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

public class ItemStoneTongs extends Item implements IAmalgamContainerItem
{
  private static final String AMALGAM_KEY = "Amalgam";
  private static final String AMOUNT_KEY = "Amount";
  public static final int CAPACITY = 9;
  private IIcon amalgamOverlay;
  private IIcon emptyOverlay;
  
  public ItemStoneTongs()
  {
    setCreativeTab(Config.tab);
    this.maxStackSize = 1;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister) {
    this.itemIcon = iconRegister.registerIcon("amalgam:stoneTongs");
    this.amalgamOverlay = iconRegister.registerIcon("amalgam:stoneTongsOverlay");
    this.emptyOverlay = iconRegister.registerIcon("amalgam:stoneTongsEmpty");
  }
  
  public IIcon getIcon(ItemStack stack, int pass)
  {
    if (pass == 0) {
      return this.itemIcon;
    }
    if (getFluidAmount(stack) > 0) {
      return this.amalgamOverlay;
    }
    
    return this.emptyOverlay;
  }
  
  public int getRenderPasses(int metadata)
  {
    return 2;
  }
  
  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack stack, int renderPass)
  {
    if ((renderPass == 1) && 
      (stack.getTagCompound() != null)) {
      if (Config.coloredAmalgam) {
        return (int)getFluid(stack).getProperties().getValue(PropertyManager.COLOR);
      }
      return (int)PropertyManager.COLOR.getDefaultValue();
    }
    

    return -1;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses()
  {
    return true;
  }
  
  public int getFluidAmount(ItemStack container) {
    if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Amalgam"))) {
      return 0;
    }
    
    NBTTagCompound containerNBT = container.stackTagCompound.getCompoundTag("Amalgam");
    return containerNBT.getInteger("Amount");
  }
  
  public AmalgamStack getFluid(ItemStack container)
  {
    if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Amalgam"))) {
      return new AmalgamStack(0, new PropertyList());
    }
    
    NBTTagCompound containerNBT = container.stackTagCompound.getCompoundTag("Amalgam");
    int amount = containerNBT.getInteger("Amount");
    PropertyList pList = new PropertyList();
    pList.readFromNBT(containerNBT.getCompoundTag("Tag"));
    return new AmalgamStack(amount, pList);
  }
  
  public int fill(ItemStack container, AmalgamStack resource, boolean doFill)
  {
    if (resource == null) {
      return 0;
    }
    
    if (!doFill) {
      if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Amalgam"))) {
        return Math.min(9, resource.amount);
      }
      
      NBTTagCompound containerNBT = container.stackTagCompound.getCompoundTag("Amalgam");
      int amount = containerNBT.getInteger("Amount");
      return Math.min(9 - amount, resource.amount);
    }
    
    if (container.stackTagCompound == null) {
      container.stackTagCompound = new NBTTagCompound();
    }
    
    if (!container.stackTagCompound.hasKey("Amalgam")) {
      NBTTagCompound amalgamTag = resource.writeToNBT(new NBTTagCompound());
      
      if (9 < resource.amount) {
        amalgamTag.setInteger("Amount", 9);
        container.stackTagCompound.setTag("Amalgam", amalgamTag);
        return 9;
      }
      
      container.stackTagCompound.setTag("Amalgam", amalgamTag);
      return resource.amount;
    }
    
    NBTTagCompound amalgamTag = container.stackTagCompound.getCompoundTag("Amalgam");
    AmalgamStack stack = AmalgamStack.loadAmalgamStackFromNBT(amalgamTag);
    
    if (stack.getFluid() != resource.getFluid()) {
      return 0;
    }
    
    int filled = 9 - stack.amount;
    
    if (resource.amount < filled) {
      stack = AmalgamStack.combine(stack, resource);
      filled = resource.amount;
    } else {
      AmalgamStack temp = new AmalgamStack(resource, filled);
      stack = AmalgamStack.combine(stack, temp);
    }
    
    container.stackTagCompound.setTag("Amalgam", stack.writeToNBT(amalgamTag));
    return filled;
  }
  
  public AmalgamStack drain(ItemStack container, int maxDrain, boolean doDrain)
  {
    if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Amalgam"))) {
      return null;
    }
    
    AmalgamStack stack = AmalgamStack.loadAmalgamStackFromNBT(container.stackTagCompound.getCompoundTag("Amalgam"));
    stack.amount = Math.min(stack.amount, maxDrain);
    
    if (doDrain) {
      if (maxDrain >= 9) {
        container.stackTagCompound.removeTag("Amalgam");
        if (container.stackTagCompound.hasNoTags()) {
          container.stackTagCompound = null;
        }
        return stack;
      }
      
      NBTTagCompound amalgamTag = container.stackTagCompound.getCompoundTag("Amalgam");
      amalgamTag.setInteger("Amount", amalgamTag.getInteger("Amount") - maxDrain);
      container.stackTagCompound.setTag("Amalgam", amalgamTag);
    }
    
    return stack;
  }
  
  public int getCapacity(ItemStack container)
  {
    return 9;
  }
  
  public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
    if ((world.getTileEntity(x, y, z) instanceof IFluidHandler)) {
      return true;
    }
    
    return false;
  }
  
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
  {
    AmalgamStack droppedAmalgam = getFluid(stack);
    ItemStack emptyTongs = new ItemStack(Config.stoneTongs);
    
    if (droppedAmalgam.amount == 0) {
      return emptyTongs;
    }
    
    ItemStack droppedBlob = new ItemStack(Config.amalgamBlob, 1);
    ((ItemAmalgamBlob)Config.amalgamBlob).setProperties(droppedBlob, droppedAmalgam.getProperties());
    ((ItemAmalgamBlob)Config.amalgamBlob).setVolume(droppedBlob, droppedAmalgam.amount);
    
    if (!world.isRemote) {
      player.entityDropItem(droppedBlob, 1.0F);
    }
    
    return emptyTongs;
  }
  
  public int getEmptySpace(ItemStack stack) {
    return 9 - getFluidAmount(stack);
  }
  
  public boolean showDurabilityBar(ItemStack stack) {
    return true;
  }
  
  public double getDurabilityForDisplay(ItemStack stack) {
    return 1.0D - getFluidAmount(stack) / 9.0F;
  }
}
