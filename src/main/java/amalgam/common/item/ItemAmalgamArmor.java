package amalgam.common.item;

import amalgam.common.Config;
import amalgam.common.properties.Property;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;

public class ItemAmalgamArmor extends ItemArmor implements amalgam.common.casting.ICastItem, ISpecialArmor
{
  public static final String ABSORB_TAG = "absorbMax";
  public IIcon[] icons = new IIcon[8];
  
  public ItemAmalgamArmor(ItemArmor.ArmorMaterial mat, int renderIndex, int armorType) {
    super(mat, renderIndex, armorType);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister)
  {
    this.icons[0] = iconRegister.registerIcon("amalgam:amalgamHelmet");
    this.icons[1] = iconRegister.registerIcon("amalgam:amalgamHelmetOverlay");
    this.icons[2] = iconRegister.registerIcon("amalgam:amalgamChestplate");
    this.icons[3] = iconRegister.registerIcon("amalgam:amalgamChestplateOverlay");
    this.icons[4] = iconRegister.registerIcon("amalgam:amalgamLeggings");
    this.icons[5] = iconRegister.registerIcon("amalgam:amalgamLeggingsOverlay");
    this.icons[6] = iconRegister.registerIcon("amalgam:amalgamBoots");
    this.icons[7] = iconRegister.registerIcon("amalgam:amalgamBootsOverlay");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack stack, int pass)
  {
    return this.icons[(this.armorType * 2 + pass)];
  }
  
  public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
    if ("overlay".equals(type)) {
      if (itemstack.getItem() == Config.amalgamLegs) {
        return "amalgam:textures/models/armor/amalgamOverlay2.png";
      }
      return "amalgam:textures/models/armor/amalgamOverlay1.png";
    }
    
    if (itemstack.getItem() == Config.amalgamLegs) {
      return "amalgam:textures/models/armor/amalgamLayer2.png";
    }
    
    return "amalgam:textures/models/armor/amalgamLayer1.png";
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List dataList, boolean b)
  {
    dataList.add(getMaxDamage(stack) - getDamage(stack) + "/" + getMaxDamage(stack));
    dataList.add(EnumChatFormatting.DARK_GREEN + "+" + getItemEnchantability(stack) + " Enchantability");
    dataList.add(EnumChatFormatting.DARK_GREEN + "+" + getArmorDisplay(player, stack, 1) + " Armor");
  }
  
  public int getItemEnchantability(ItemStack stack)
  {
    if (stack.getTagCompound() == null) {
      return 0;
    }
    return stack.getTagCompound().getInteger("enchantability");
  }
  
  public int getMaxDamage(ItemStack stack)
  {
    if (stack.getTagCompound() == null) {
      return 1;
    }
    return stack.getTagCompound().getInteger("durability");
  }
  



  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack stack, int renderPass)
  {
    if (renderPass == 1) {
      return 16777215;
    }
    
    if (!Config.coloredAmalgam) {
      return (int)PropertyManager.COLOR.getDefaultValue();
    }
    
    if ((stack.hasTagCompound()) && (stack.stackTagCompound.hasKey("color"))) {
      return stack.stackTagCompound.getInteger("color");
    }
    
    return 16777215;
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
    float hardness = pList.getValue(PropertyManager.HARDNESS);
    float maliability = pList.getValue(PropertyManager.MALIABILITY);
    int color = (int)pList.getValue(PropertyManager.COLOR);
    
    toolTag.setInteger("color", color);
    toolTag.setInteger("enchantability", (int)(density * 0.1D + hardness * 0.2D + luster * 2.5D + maliability * 0.3D));
    float armorTypeFactor = 1.0F;
    
    switch (this.armorType) {
    case 0: 
      armorTypeFactor = 0.6875F;
      break;
    case 1: 
      armorTypeFactor = 1.0F;
      break;
    case 2: 
      armorTypeFactor = 0.9375F;
      break;
    default: 
      armorTypeFactor = 0.8125F;
    }
    
    
    int maxDurability = (int)(density * 200.0F + hardness * 75.0F + luster * 2.0F + maliability * 3.0F);
    toolTag.setInteger("durability", maxDurability);
    
    switch (this.armorType) {
    case 0: 
      armorTypeFactor = 0.375F;
      break;
    case 1: 
      armorTypeFactor = 1.0F;
      break;
    case 2: 
      armorTypeFactor = 0.75F;
      break;
    default: 
      armorTypeFactor = 0.375F;
    }
    
    
    toolTag.setInteger("absorbMax", (int)(density * 0.2D + hardness * 0.2D + luster * 0.05D + maliability * 0.6D));
    returnStack.setTagCompound(toolTag);
    
    return returnStack;
  }
  
  public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
  {
    float absorbRatio = armor.getTagCompound().getInteger("absorbMax") * 0.04F;
    int absorbMax = (int)(absorbRatio * 20.0F);
    int priority = slot;
    
    return new ISpecialArmor.ArmorProperties(priority, absorbRatio, absorbMax);
  }
  
  public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
  {
    if (armor.getTagCompound() == null) {
      return 1;
    }
    
    return armor.getTagCompound().getInteger("absorbMax");
  }
  
  public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
  {
    stack.damageItem(1, entity);
  }
  
  public boolean hasColor(ItemStack stack)
  {
    return true;
  }
  
  public int getColor(ItemStack stack) {
    if (!Config.coloredAmalgam) {
      return (int)PropertyManager.COLOR.getDefaultValue();
    }
    
    if ((stack.hasTagCompound()) && (stack.stackTagCompound.hasKey("color"))) {
      return stack.stackTagCompound.getInteger("color");
    }
    
    return 16777215;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses()
  {
    return true;
  }
}
