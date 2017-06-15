package amalgam.common.item;

import amalgam.common.Config;
import amalgam.common.casting.ICastItem;
import amalgam.common.properties.Property;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.EnumHelper;

public class ItemAmalgamTool
  extends ItemTool
  implements ICastItem
{
  public static Item.ToolMaterial toolMatAmalgam = EnumHelper.addToolMaterial("AMALGAM", 0, 1, 0.0F, 0.0F, 0);
  
  public static final String DAMAGE_TAG = "damage";
  
  public static final String DURABILITY_TAG = "durability";
  
  public static final String EFFICIENCY_TAG = "efficiency";
  public static final String HARVEST_TAG = "harvest level";
  public static final String ENCHANTABILITY_TAG = "enchantability";
  public static final String COLOR_TAG = "color";
  public static final String TOOL_CLASS_PICK = "pickaxe";
  public static final String TOOL_CLASS_AXE = "axe";
  public static final String TOOL_CLASS_SHOVEL = "shovel";
  private final String toolClass;
  private final float damageMod;
  protected IIcon hilt;
  
  protected ItemAmalgamTool(float damageMod, String toolClass, Set<Block> blocks)
  {
    super(damageMod, toolMatAmalgam, blocks);
    this.toolClass = toolClass;
    this.damageMod = damageMod;
  }
  
  public boolean hitEntity(ItemStack stack, EntityLivingBase entityBeingHit, EntityLivingBase entityHitting)
  {
    EntityPlayer player = (EntityPlayer)entityHitting;
    float damage = getDamageVsEntity(stack);
    DamageSource damageSource = DamageSource.causePlayerDamage(player);
    entityBeingHit.attackEntityFrom(damageSource, damage);
    
    return super.hitEntity(stack, entityBeingHit, entityHitting);
  }
  
  public float getDigSpeed(ItemStack stack, Block block, int meta)
  {
    if (ForgeHooks.isToolEffective(stack, block, meta)) {
      return getEfficiency(stack);
    }
    
    return super.getDigSpeed(stack, block, meta);
  }
  
  public int getHarvestLevel(ItemStack stack, String toolClass)
  {
    if ((toolClass != null) && (toolClass.equals(this.toolClass))) {
      if (stack.getTagCompound() == null) {
        return 0;
      }
      return stack.getTagCompound().getInteger("harvest level");
    }
    return super.getHarvestLevel(stack, toolClass);
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
  
  public float getDamageVsEntity(ItemStack stack) {
    if (stack.getTagCompound() == null) {
      return 0.0F;
    }
    
    return stack.getTagCompound().getFloat("damage");
  }
  
  public float getEfficiency(ItemStack stack) {
    if (stack.getTagCompound() == null) {
      return 1.0F;
    }
    
    return stack.getTagCompound().getFloat("efficiency");
  }
  
  public Multimap getItemAttributeModifiers()
  {
    return HashMultimap.create();
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List dataList, boolean b)
  {
    dataList.add(getMaxDamage(stack) - getDamage(stack) + "/" + getMaxDamage(stack));
    dataList.add(EnumChatFormatting.DARK_GREEN + "+" + getItemEnchantability(stack) + " Enchantability");
    dataList.add(EnumChatFormatting.DARK_GREEN + "+" + (int)getEfficiency(stack) + " Efficiency");
    dataList.add(EnumChatFormatting.DARK_GREEN + "+" + getHarvestLevel(stack, this.toolClass) + " Harvest Level");
    dataList.add(EnumChatFormatting.BLUE + "+" + String.format("%.1f", new Object[] { Float.valueOf(getDamageVsEntity(stack)) }) + " Attack Damage");
  }
  
  public ItemStack generateStackWithProperties(PropertyList pList, ItemStack[] materials, int stackSize)
  {
    ItemStack returnStack = new ItemStack(this, stackSize);
    NBTTagCompound toolTag = new NBTTagCompound();
    
    if (pList == null) {
      toolTag.setInteger("color", (int)PropertyManager.COLOR.getDefaultValue());
      returnStack.setTagCompound(toolTag);
      return returnStack;
    }
    
    float luster = pList.getValue(PropertyManager.LUSTER);
    float density = pList.getValue(PropertyManager.DENSITY);
    float hardness = pList.getValue(PropertyManager.HARDNESS);
    float maliability = pList.getValue(PropertyManager.MALIABILITY);
    int color = (int)pList.getValue(PropertyManager.COLOR);
    
    toolTag.setInteger("harvest level", (int)(hardness * 0.5D));
    toolTag.setInteger("enchantability", (int)(density * 0.1D + hardness * 0.2D + luster * 2.5D + maliability * 0.3D));
    toolTag.setFloat("efficiency", 1.8F * luster + 0.7F * maliability);
    int maxDurability = (int)(density * 200.0F + hardness * 75.0F + luster * 2.0F + maliability * 3.0F);
    
    toolTag.setInteger("durability", maxDurability);
    toolTag.setFloat("damage", hardness * 0.1F + maliability * 0.7F + this.damageMod);
    toolTag.setInteger("color", color);
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
