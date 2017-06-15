package amalgam.common.item;

import amalgam.common.Config;
import amalgam.common.casting.ICastItem;
import amalgam.common.properties.Property;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemAmalgamShroom extends ItemFood implements ICastItem
{
  private static final String EFFECT_NUM_TAG = "num";
  private static final String DURATION_MOD_TAG = "dur";
  private static final String EFFECT_MOD_TAG = "eff";
  private static final String COLOR_TAG = "color";
  IIcon base;
  
  public ItemAmalgamShroom()
  {
    super(1, false);
    setAlwaysEdible();
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister) {
    this.itemIcon = iconRegister.registerIcon("amalgam:amalgamShroomTop");
    this.base = iconRegister.registerIcon("amalgam:amalgamShroomBase");
  }
  
  protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
  {
    if ((!world.isRemote) && (stack.hasTagCompound()))
    {

      Random random = new Random();
      int effects = stack.stackTagCompound.getInteger("num");
      int durMod = stack.stackTagCompound.getInteger("dur");
      int effMod = stack.stackTagCompound.getInteger("eff");
      
      for (int i = 0; i < effects; i++) {
        int duration = random.nextInt(100) + 100 + 20 * random.nextInt(durMod);
        player.addPotionEffect(new PotionEffect(random.nextInt(22), duration, random.nextInt(effMod)));
      }
    }
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
    
    toolTag.setInteger("num", (int)(luster / 3.0D + 1.0D));
    toolTag.setInteger("dur", (int)(density + hardness));
    toolTag.setInteger("eff", (int)(maliability / 3.0D + 1.0D));
    
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
    
    return this.base;
  }
}
