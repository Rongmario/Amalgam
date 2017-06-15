package amalgam.common.item;

import amalgam.common.Config;
import amalgam.common.properties.Property;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class ItemAmalgamHoe extends ItemHoe implements amalgam.common.casting.ICastItem
{
  private IIcon hilt;
  
  public ItemAmalgamHoe()
  {
    super(ItemAmalgamTool.toolMatAmalgam);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister) {
    this.itemIcon = iconRegister.registerIcon("amalgam:amalgamHoeBlade");
    this.hilt = iconRegister.registerIcon("amalgam:amalgamHoeHilt");
  }
  
  public int getMaxDamage(ItemStack stack)
  {
    if (stack.getTagCompound() == null) {
      return 1;
    }
    
    return stack.getTagCompound().getInteger("durability");
  }
  
  public int getItemEnchantability(ItemStack stack)
  {
    if (stack.getTagCompound() == null) {
      return 0;
    }
    
    return stack.getTagCompound().getInteger("enchantability");
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List dataList, boolean b)
  {
    dataList.add(getMaxDamage(stack) - getDamage(stack) + "/" + getMaxDamage(stack));
    dataList.add(EnumChatFormatting.DARK_GREEN + "+" + getItemEnchantability(stack) + " Enchantability");
  }
  
  public ItemStack generateStackWithProperties(PropertyList pList, ItemStack[] materials, int stackSize)
  {
    ItemStack returnStack = new ItemStack(this, stackSize);
    NBTTagCompound toolTag = new NBTTagCompound();
    
    if (pList == null) {
      returnStack.setTagCompound(toolTag);
      return returnStack;
    }
    
    float luster = pList.getValue(PropertyManager.LUSTER);
    float density = pList.getValue(PropertyManager.DENSITY);
    float maliability = pList.getValue(PropertyManager.MALIABILITY);
    float hardness = pList.getValue(PropertyManager.HARDNESS);
    int color = (int)pList.getValue(PropertyManager.COLOR);
    
    toolTag.setInteger("color", color);
    toolTag.setInteger("enchantability", (int)(density * 0.1D + hardness * 0.2D + luster * 2.5D + maliability * 0.3D));
    int maxDurability = (int)(density * 200.0F + hardness * 75.0F + luster * 2.0F + maliability * 3.0F);
    toolTag.setInteger("durability", maxDurability);
    returnStack.setTagCompound(toolTag);
    
    return returnStack;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses()
  {
    return true;
  }
  
  public int getRenderPasses(int metadata)
  {
    return 2;
  }
  
  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack stack, int renderPass)
  {
    if (renderPass == 1) {
      if (!Config.coloredAmalgam) {
        return (int)PropertyManager.COLOR.getDefaultValue();
      }
      if ((stack.hasTagCompound()) && (stack.stackTagCompound.hasKey("color"))) {
        return stack.stackTagCompound.getInteger("color");
      }
      
      return 16777215;
    }
    
    return -1;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack stack, int renderPass) {
    if (renderPass == 1) {
      return this.itemIcon;
    }
    
    return this.hilt;
  }
}
