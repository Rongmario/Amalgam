package amalgam.common.item;

import amalgam.common.casting.ICastItem;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;


public class ItemAmalgamShovel
  extends ItemAmalgamTool
  implements ICastItem
{
  private static final Set<Block> USABLE_BLOCKS = Sets.newHashSet(new Block[] { Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium });
  
  public ItemAmalgamShovel()
  {
    super(1.0F, "shovel", USABLE_BLOCKS);
    setHarvestLevel("shovel", 0);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister) {
    this.itemIcon = iconRegister.registerIcon("amalgam:amalgamShovelBlade");
    this.hilt = iconRegister.registerIcon("amalgam:amalgamShovelHilt");
  }
  
  public boolean canHarvestBlock(Block block, ItemStack stack)
  {
    return block == Blocks.snow_layer;
  }
}
