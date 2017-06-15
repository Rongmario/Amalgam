package amalgam.common.item;

import amalgam.common.entity.EntityAmalgamPotato;
import amalgam.common.properties.Property;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemAmalgamPotato extends Item implements amalgam.common.casting.ICastItem
{
  public static final String EXPLOSION_TAG = "exp";
  public static final String VELOCITY_TAG = "vel";
  public static final String DAMAGE_TAG = "dmg";
  IIcon base;
  private static final String COLOR_TAG = "color";
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister)
  {
    this.itemIcon = iconRegister.registerIcon("amalgam:amalgamPotatoOverlay");
    this.base = iconRegister.registerIcon("amalgam:amalgamPotatoBase");
  }
  
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    if (!player.capabilities.isCreativeMode) {
      stack.stackSize -= 1;
    }
    
    world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    
    if (!world.isRemote) {
      NBTTagCompound comp = stack.stackTagCompound;
      if (comp != null) {
        float damage = comp.getFloat("dmg");
        float velocity = comp.getFloat("vel");
        float explosion = comp.getFloat("exp");
        int color = comp.getInteger("color");
        world.spawnEntityInWorld(new EntityAmalgamPotato(world, player, velocity, explosion, damage, color));
      }
    }
    
    return stack;
  }
  
  public ItemStack generateStackWithProperties(PropertyList pList, ItemStack[] items, int stackSize)
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
    
    float v = 0.2F + 2.5F / density;
    
    if (v > 1.5D) {
      v = 1.5F;
    }
    toolTag.setFloat("vel", v);
    toolTag.setFloat("exp", luster / 7.0F + 0.5F);
    toolTag.setFloat("dmg", maliability / 4.0F);
    
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
      if (!amalgam.common.Config.coloredAmalgam) {
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
    
    return this.base;
  }
}
